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
package org.sonar.api.notifications;

import com.google.common.collect.Maps;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Map;

/**
 * <p>
 * This class represents a notification that will be delivered to users. This is a general concept and it has no
 * knowledge of the possible ways to be delivered (see {@link NotificationChannel}) or of the users who should
 * receive it (see {@link NotificationDispatcher}).
 * </p>
 * <p>
 * When creating a new notification, it is strongly advised to give a default message that can be  used by channels
 * that don't want to specifically format messages for different notification types. You can use
 * {@link Notification#setDefaultMessage(String)} for that purpose.
 * </p>
 *
 * @since 2.10
 */
public class Notification implements Serializable {

  private static final String DEFAULT_MESSAGE_KEY = "default_message";

  private final String type;
  private final Map<String, String> fields = Maps.newHashMap();

  /**
   * <p>
   * Create a new {@link Notification} of the given type.
   * </p>
   * Example: type = "new-violations"
   *
   * @param type the type of notification
   */
  public Notification(String type) {
    this.type = type;
  }

  /**
   * Returns the type of the notification
   *
   * @return the type
   */
  public String getType() {
    return type;
  }

  /**
   * <p>
   * When creating a new notification, it is strongly advised to give a default message that can be
   * used by channels that don't want to specifically format messages for different notification types.
   * </p>
   * <p>
   * This method is equivalent to setting a value for the field {@link #DEFAULT_MESSAGE_KEY} with
   * {@link #setFieldValue(String, String)}.
   * </p>
   *
   * @since 3.5
   */
  public Notification setDefaultMessage(String value) {
    setFieldValue(DEFAULT_MESSAGE_KEY, value);
    return this;
  }

  /**
   * Returns the default message to display for this notification.
   */
  public String getDefaultMessage() {
    String defaultMessage = getFieldValue(DEFAULT_MESSAGE_KEY);
    if (defaultMessage == null) {
      defaultMessage = this.toString();
    }
    return defaultMessage;
  }

  /**
   * Adds a field (kind of property) to the notification
   *
   * @param field the name of the field (= the key)
   * @param value the value of the field
   * @return the notification itself
   */
  public Notification setFieldValue(String field, @Nullable String value) {
    fields.put(field, value);
    return this;
  }

  /**
   * Returns the value of a field.
   *
   * @param field the field
   * @return the value of the field
   */
  @CheckForNull
  public String getFieldValue(String field) {
    return fields.get(field);
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Notification)) {
      return false;
    }
    if (this == obj) {
      return true;
    }
    Notification other = (Notification) obj;
    return this.type.equals(other.type) && this.fields.equals(other.fields);
  }

  @Override
  public int hashCode() {
    return type.hashCode() * 31 + fields.hashCode();
  }

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
  }

}
