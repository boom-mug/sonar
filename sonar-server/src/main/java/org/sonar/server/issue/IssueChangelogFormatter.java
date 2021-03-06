/*
 * SonarQube, open source software quality management tool.
 * Copyright (C) 2008-2013 SonarSource
 * mailto:contact AT sonarsource DOT com
 *
 * SonarQube is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * SonarQube is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.server.issue;

import org.sonar.api.ServerComponent;
import org.sonar.api.i18n.I18n;
import org.sonar.api.issue.internal.FieldDiffs;
import org.sonar.api.utils.Duration;
import org.sonar.core.issue.IssueUpdater;
import org.sonar.server.user.UserSession;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;

public class IssueChangelogFormatter implements ServerComponent {

  private static final String ISSUE_CHANGELOG_FIELD = "issue.changelog.field.";

  private final I18n i18n;

  public IssueChangelogFormatter(I18n i18n) {
    this.i18n = i18n;
  }

  public List<String> format(Locale locale, FieldDiffs diffs) {
    List<String> result = newArrayList();
    for (Map.Entry<String, FieldDiffs.Diff> entry : diffs.diffs().entrySet()) {
      StringBuilder message = new StringBuilder();
      String key = entry.getKey();
      IssueChangelogDiffFormat diffFormat = format(locale, key, entry.getValue());
      if (diffFormat.newValue() != null) {
        message.append(i18n.message(locale, "issue.changelog.changed_to", null, i18n.message(locale, ISSUE_CHANGELOG_FIELD + key, null), diffFormat.newValue()));
      } else {
        message.append(i18n.message(locale, "issue.changelog.removed", null, i18n.message(locale, ISSUE_CHANGELOG_FIELD + key, null)));
      }
      if (diffFormat.oldValue() != null) {
        message.append(" (");
        message.append(i18n.message(locale, "issue.changelog.was", null, diffFormat.oldValue()));
        message.append(")");
      }
      result.add(message.toString());
    }
    return result;
  }

  private IssueChangelogDiffFormat format(Locale locale, String key, FieldDiffs.Diff diff) {
    Serializable newValue = diff.newValue();
    Serializable oldValue = diff.oldValue();

    String newValueString = newValue != null && !"".equals(newValue) ? newValue.toString() : null;
    String oldValueString = oldValue != null && !"".equals(oldValue) ? oldValue.toString() : null;
    if (IssueUpdater.TECHNICAL_DEBT.equals(key)) {
      if (newValueString != null) {
        newValueString = i18n.formatWorkDuration(UserSession.get().locale(), Duration.create(Long.parseLong(newValueString)));
      }
      if (oldValueString != null) {
        oldValueString = i18n.formatWorkDuration(UserSession.get().locale(), Duration.create(Long.parseLong(oldValueString)));
      }
    }
    return new IssueChangelogDiffFormat(oldValueString, newValueString);
  }

}
