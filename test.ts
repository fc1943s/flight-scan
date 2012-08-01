class Greeter {
}

var casper, result, utils;

casper = require('casper').create({
    verbose: true,
    logLevel: 'error',
    waitTimeout: 10000,
    pageSettings: {
        loadImages: false,
        loadPlugins: false,
        userAgent: 'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.86 Safari/537.36 OPR/33.0.1990.115'
    },
    onWaitTimeout: function () {
        return this.capture('waitTimeout.png');
    }
});

utils = require('utils');

result = {};

casper.on('page.error', function (msg, trace) {
    this.echo('Error: #{msg} - ' + msg, 'ERROR');
    this.echo('file: #{trace[0].file} - ' + trace[0].file, 'WARNING');
    this.echo('line: ' + trace[0].line, 'WARNING');
    return this.echo('function: ' + trace[0]['function'], 'WARNING');
});

casper.on('remote.message', function (message) {
    return this.echo('Log: ' + message);
});

casper.viewport(500, 900);

casper.then(function () {
    return result = this.evaluate(function () {
        var data, getFlightInfo;
        getFlightInfo = function (flightDom) {
            return {
                start: flightDom.find('.time-start').html(),
                end: flightDom.find('.time-end').html(),
                duration: flightDom.find('.flight-duration').html(),
                stops: parseInt(flightDom.find('.routes').html().trim()[0])
            };
        };
        data = [];
        $('li.flight').each(function (k, v) {
            var price;
            price = parseInt($(v).find('span.price').html());
            return $(v).find('li.flightDetail').each(function (k2, v2) {
                return data.push({
                    price: price,
                    dep: getFlightInfo($($(v2).find('.departure-flight'))),
                    ret: getFlightInfo($($(v2).find('.return-flight')))
                });
            });
        });
        return data;
    });
});

casper.run(function () {
    utils.dump(result);
    this.capture('test.png');
    return this.exit();
});
