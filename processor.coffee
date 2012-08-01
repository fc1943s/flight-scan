casper = require('casper').create(
  verbose: true
  logLevel: 'error'
  waitTimeout: 10000
  pageSettings:
    loadImages: false
    loadPlugins: false
    userAgent: 'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.86 Safari/537.36 OPR/33.0.1990.115'
  onWaitTimeout: ->
    @capture 'waitTimeout.png'
)

utils = require('utils')
result = {}

casper.on 'page.error', (msg, trace) ->
  @echo 'Error: #{msg} - ' + msg, 'ERROR'
  @echo 'file: #{trace[0].file} - ' + trace[0].file, 'WARNING'
  @echo 'line: ' + trace[0].line, 'WARNING'
  @echo 'function: ' + trace[0]['function'], 'WARNING'

casper.on 'remote.message', (message) ->
  @echo 'Log: ' + message

casper.viewport 500, 900

casper.then ->
  result = @evaluate ->
    getFlightInfo = (flightDom) ->
      start: flightDom.find('.time-start').html()
      end: flightDom.find('.time-end').html()
      duration: flightDom.find('.flight-duration').html()
      stops: parseInt(flightDom.find('.routes').html().trim()[0])
    
    data = []
    $('li.flight').each (k, v) ->
      price = parseInt($(v).find('span.price').html())
      $(v).find('li.flightDetail').each (k2, v2) ->
        data.push
          price: price
          dep: getFlightInfo($($(v2).find('.departure-flight')))
          ret: getFlightInfo($($(v2).find('.return-flight')))
    data

casper.run ->
  utils.dump result
  @capture 'test.png'
  @exit()