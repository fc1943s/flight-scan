window.$.getFlightList = ->
  data = []

  getFlightInfo = (flightDom) ->
    {
      start: flightDom.find('.time-start').html()
      end: flightDom.find('.time-end').html()
      duration: flightDom.find('.flight-duration').html()
      stops: parseInt(flightDom.find('.routes').html().trim()[0])
    }

  $('li.flight').each (k, v) ->
    price = parseInt($(v).find('span.price').html())
    $(v).find('li.flightDetail').each (k2, v2) ->
      data.push
        price: price
        dep: getFlightInfo($($(v2).find('.departure-flight')))
        ret: getFlightInfo($($(v2).find('.return-flight')))
      return
    return
  data