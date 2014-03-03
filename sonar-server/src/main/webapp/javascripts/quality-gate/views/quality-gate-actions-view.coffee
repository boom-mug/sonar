define [
  'backbone.marionette',
  'handlebars',
  'cs!quality-gate/models/quality-gate'
], (
  Marionette,
  Handlebars,
  QualityGate
) ->

  class QualityGateActionsView extends Marionette.ItemView
    template: Handlebars.compile jQuery('#quality-gate-actions-template').html()


    events:
      'click #quality-gate-add': 'add'


    add: ->
      qualityGate = new QualityGate()
      @options.app.qualityGateEditView.model = qualityGate
      @options.app.qualityGateEditView.show()
