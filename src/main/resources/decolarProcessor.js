var params = {};
if(typeof tmpParams !== 'undefined')
{
	params = tmpParams;
}

var callback = function(result){ console.log(result); };
if(typeof arguments !== 'undefined')
{
	callback = arguments[arguments.length - 1];
}



window.onbeforeunload = undefined;
$.fx.off = true;


var styleTag = $('<style>* { transition-property: none !important; transform: none !important; animation: none !important; }</style>');
$('html > head').append(styleTag);

var foundBest = 0;
var interval = undefined;

var result =
{
	price : -1,
	code : '',
	info : ''
}

var finish = function()
{
	clearInterval(interval);
	if(result.price == -30)
	{
		result.price = -20;
		console.log(result);
		setTimeout(function(){ callback(JSON.stringify(result)); }, 15000);
	}
	else
	{
		callback(JSON.stringify(result));
	}
};

interval = setInterval(function()
{
	if ($('#no-results-alternatives-error:visible').length == 1 || $('#no-results-error:visible').length == 1)
	{
		finish();
	}
	
	if ($('#streaming-error:visible').length == 1)
	{
		result.price = -20;
		finish();
	}
	
	if($('#captcha-form:visible').length == 1)
	{
		result.price = -30;
		finish();
	}

	$('.flights-tab-airlinePricesMatrix').click();
	$('#currency').val('BRL');
	$('#currency').change();

	var prices = $('.price-best .price-amount');

	if (prices.length === 0)
	{
		return;
	}

	prices.each(function(k, v)
	{
		var data = $(v).parents('.price-best').data();

		if (params.scale)
		{
			if (data.scaleIndex == params.scale)
			{
				foundBest = 1;
			}
			else
			{
				return;
			}
		}
		else
		{
			foundBest = 1;
		}

		console.log('1: ' + $(v).html());
		var price = Number($(v).html().replace('.',''));

		if (price < result.price || result.price == -1)
		{
			result.price = price;
			
		}
	});

	if (foundBest === 0)
	{
		finish();
	}
	if (result.price > 0)
	{
		$('.cluster').each(function(k, v)
		{
			console.log('2: ' + $(v).find('.price-amount').html());
			if (Number($(v).find('.price-amount').html().replace('.','')) == result.price)
			{
				$(v).find('.stops a').each(function(k2, v2)
				{
					console.log('3: ' + $(v2).find('.stops-text pluralize').html());
					result.info += ($(v2).parents('trip-route').find('.ux-common-icon-airplane-going').length>0 ? 'ida. ' : 'volta. ') + 'sai: ' + $(v2).parents('ul').find('.leave .hour').html() + '. ' + $(v2).children('.duration-text').html() + '. ' + $(v2).find('.stops-text').html().replace('&nbsp;',' ') + '   #   ';
					result.info = result.info.replace(/ +/g,' ');
				});
			}
		});

		finish();
	}
}, 1500);