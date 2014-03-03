define [
  'backbone',
  'cs!quality-gate/models/quality-gate',
  'cs!quality-gate/views/quality-gate-detail-view',
  'cs!quality-gate/views/quality-gate-detail-header-view',
], (
  Backbone,
  QualityGate,
  QualityGateDetailView,
  QualityGateDetailHeaderView
) ->

  class QualityGateRouter extends Backbone.Router

    routes:
      'show/:id': 'show'


    initialize: (options) ->
      @app = options.app


    show: (id) ->
      qualityGate = @app.qualityGates.get id
      if qualityGate
        @app.qualityGateSidebarListView.highlight id

        qualityGateDetailHeaderView = new QualityGateDetailHeaderView
          app: @app
          model: qualityGate
        @app.layout.headerRegion.show qualityGateDetailHeaderView

        qualityGateDetailView = new QualityGateDetailView
          app: @app
          model: qualityGate
        @app.layout.detailsRegion.show qualityGateDetailView
        qualityGateDetailView.$el.hide()

        qualityGateDetailHeaderView.showSpinner()
        qualityGate.fetch().done ->
          qualityGateDetailView.$el.show()
          qualityGateDetailHeaderView.hideSpinner()
