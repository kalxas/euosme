function euosmegwt(){
  var $wnd_0 = window, $doc_0 = document, $stats = $wnd_0.__gwtStatsEvent?function(a){
    return $wnd_0.__gwtStatsEvent(a);
  }
  :null, $sessionId_0 = $wnd_0.__gwtStatsSessionId?$wnd_0.__gwtStatsSessionId:null, scriptsDone, loadDone, bodyDone, base = '', metaProps = {}, values = [], providers = [], answers = [], softPermutationId = 0, onLoadErrorFunc, propertyErrorFunc;
  $stats && $stats({moduleName:'euosmegwt', sessionId:$sessionId_0, subSystem:'startup', evtGroup:'bootstrap', millis:(new Date).getTime(), type:'begin'});
  if (!$wnd_0.__gwt_stylesLoaded) {
    $wnd_0.__gwt_stylesLoaded = {};
  }
  if (!$wnd_0.__gwt_scriptsLoaded) {
    $wnd_0.__gwt_scriptsLoaded = {};
  }
  function isHostedMode(){
    var result = false;
    try {
      var query = $wnd_0.location.search;
      return (query.indexOf('gwt.codesvr=') != -1 || (query.indexOf('gwt.hosted=') != -1 || $wnd_0.external && $wnd_0.external.gwtOnLoad)) && query.indexOf('gwt.hybrid') == -1;
    }
     catch (e) {
    }
    isHostedMode = function(){
      return result;
    }
    ;
    return result;
  }

  function maybeStartModule(){
    if (scriptsDone && loadDone) {
      var iframe = $doc_0.getElementById('euosmegwt');
      var frameWnd = iframe.contentWindow;
      if (isHostedMode()) {
        frameWnd.__gwt_getProperty = function(name_0){
          return computePropValue(name_0);
        }
        ;
      }
      euosmegwt = null;
      frameWnd.gwtOnLoad(onLoadErrorFunc, 'euosmegwt', base, softPermutationId);
      $stats && $stats({moduleName:'euosmegwt', sessionId:$sessionId_0, subSystem:'startup', evtGroup:'moduleStartup', millis:(new Date).getTime(), type:'end'});
    }
  }

  function computeScriptBase(){
    if (metaProps['baseUrl']) {
      base = metaProps['baseUrl'];
      return base;
    }
    var thisScript;
    var scriptTags = $doc_0.getElementsByTagName('script');
    for (var i = 0; i < scriptTags.length; ++i) {
      if (scriptTags[i].src.indexOf('euosmegwt.nocache.js') != -1) {
        thisScript = scriptTags[i];
      }
    }
    if (!thisScript) {
      var markerId = '__gwt_marker_euosmegwt';
      var markerScript;
      $doc_0.write('<script id="' + markerId + '"><\/script>');
      markerScript = $doc_0.getElementById(markerId);
      thisScript = markerScript && markerScript.previousSibling;
      while (thisScript && thisScript.tagName != 'SCRIPT') {
        thisScript = thisScript.previousSibling;
      }
    }
    function getDirectoryOfFile(path){
      var hashIndex = path.lastIndexOf('#');
      if (hashIndex == -1) {
        hashIndex = path.length;
      }
      var queryIndex = path.indexOf('?');
      if (queryIndex == -1) {
        queryIndex = path.length;
      }
      var slashIndex = path.lastIndexOf('/', Math.min(queryIndex, hashIndex));
      return slashIndex >= 0?path.substring(0, slashIndex + 1):'';
    }

    ;
    if (thisScript && thisScript.src) {
      base = getDirectoryOfFile(thisScript.src);
    }
    if (base == '') {
      var baseElements = $doc_0.getElementsByTagName('base');
      if (baseElements.length > 0) {
        base = baseElements[baseElements.length - 1].href;
      }
       else {
        base = getDirectoryOfFile($doc_0.location.href);
      }
    }
     else if (base.match(/^\w+:\/\//)) {
    }
     else {
      var img = $doc_0.createElement('img');
      img.src = base + 'clear.cache.gif';
      base = getDirectoryOfFile(img.src);
    }
    if (markerScript) {
      markerScript.parentNode.removeChild(markerScript);
    }
    return base;
  }

  function processMetas(){
    var metas = document.getElementsByTagName('meta');
    for (var i = 0, n = metas.length; i < n; ++i) {
      var meta = metas[i], name_0 = meta.getAttribute('name'), content_0;
      if (name_0) {
        name_0 = name_0.replace('euosmegwt::', '');
        if (name_0.indexOf('::') >= 0) {
          continue;
        }
        if (name_0 == 'gwt:property') {
          content_0 = meta.getAttribute('content');
          if (content_0) {
            var value, eq = content_0.indexOf('=');
            if (eq >= 0) {
              name_0 = content_0.substring(0, eq);
              value = content_0.substring(eq + 1);
            }
             else {
              name_0 = content_0;
              value = '';
            }
            metaProps[name_0] = value;
          }
        }
         else if (name_0 == 'gwt:onPropertyErrorFn') {
          content_0 = meta.getAttribute('content');
          if (content_0) {
            try {
              propertyErrorFunc = eval(content_0);
            }
             catch (e) {
              alert('Bad handler "' + content_0 + '" for "gwt:onPropertyErrorFn"');
            }
          }
        }
         else if (name_0 == 'gwt:onLoadErrorFn') {
          content_0 = meta.getAttribute('content');
          if (content_0) {
            try {
              onLoadErrorFunc = eval(content_0);
            }
             catch (e) {
              alert('Bad handler "' + content_0 + '" for "gwt:onLoadErrorFn"');
            }
          }
        }
      }
    }
  }

  function __gwt_isKnownPropertyValue(propName, propValue){
    return propValue in values[propName];
  }

  function __gwt_getMetaProperty(name_0){
    var value = metaProps[name_0];
    return value == null?null:value;
  }

  function unflattenKeylistIntoAnswers(propValArray, value){
    var answer = answers;
    for (var i = 0, n = propValArray.length - 1; i < n; ++i) {
      answer = answer[propValArray[i]] || (answer[propValArray[i]] = []);
    }
    answer[propValArray[n]] = value;
  }

  function computePropValue(propName){
    var value = providers[propName](), allowedValuesMap = values[propName];
    if (value in allowedValuesMap) {
      return value;
    }
    var allowedValuesList = [];
    for (var k in allowedValuesMap) {
      allowedValuesList[allowedValuesMap[k]] = k;
    }
    if (propertyErrorFunc) {
      propertyErrorFunc(propName, allowedValuesList, value);
    }
    throw null;
  }

  var frameInjected;
  function maybeInjectFrame(){
    if (!frameInjected) {
      frameInjected = true;
      var iframe = $doc_0.createElement('iframe');
      iframe.src = "javascript:''";
      iframe.id = 'euosmegwt';
      iframe.style.cssText = 'position:absolute;width:0;height:0;border:none';
      iframe.tabIndex = -1;
      $doc_0.body.appendChild(iframe);
      $stats && $stats({moduleName:'euosmegwt', sessionId:$sessionId_0, subSystem:'startup', evtGroup:'moduleStartup', millis:(new Date).getTime(), type:'moduleRequested'});
      iframe.contentWindow.location.replace(base + initialHtml);
    }
  }

  providers['locale'] = function(){
    try {
      var locale;
      var defaultLocale = 'en' || 'default';
      if (locale == null) {
        var args = location.search;
        var startLang = args.indexOf('locale=');
        if (startLang >= 0) {
          var language = args.substring(startLang);
          var begin = language.indexOf('=') + 1;
          var end = language.indexOf('&');
          if (end == -1) {
            end = language.length;
          }
          locale = language.substring(begin, end);
        }
      }
      if (locale == null) {
        locale = __gwt_getMetaProperty('locale');
      }
      if (locale == null) {
        locale = $wnd_0['__gwt_Locale'];
      }
       else {
        $wnd_0['__gwt_Locale'] = locale || defaultLocale;
      }
      if (locale == null) {
        return defaultLocale;
      }
      while (!__gwt_isKnownPropertyValue('locale', locale)) {
        var lastIndex = locale.lastIndexOf('_');
        if (lastIndex == -1) {
          locale = defaultLocale;
          break;
        }
         else {
          locale = locale.substring(0, lastIndex);
        }
      }
      return locale;
    }
     catch (e) {
      alert('Unexpected exception in locale detection, using default: ' + e);
      return 'default';
    }
  }
  ;
  values['locale'] = {bg:0, cs:1, da:2, de:3, 'default':4, el:5, en:6, es:7, et:8, fi:9, fr:10, hu:11, it:12, lt:13, lv:14, mt:15, nl:16, pl:17, pt:18, ro:19, sk:20, sl:21, sv:22};
  providers['user.agent'] = function(){
    var ua = navigator.userAgent.toLowerCase();
    var makeVersion = function(result){
      return parseInt(result[1]) * 1000 + parseInt(result[2]);
    }
    ;
    if (ua.indexOf('opera') != -1) {
      return 'opera';
    }
     else if (ua.indexOf('webkit') != -1) {
      return 'safari';
    }
     else if (ua.indexOf('msie') != -1) {
      if (document.documentMode >= 8) {
        return 'ie8';
      }
       else {
        var result_0 = /msie ([0-9]+)\.([0-9]+)/.exec(ua);
        if (result_0 && result_0.length == 3) {
          var v = makeVersion(result_0);
          if (v >= 6000) {
            return 'ie6';
          }
        }
      }
    }
     else if (ua.indexOf('gecko') != -1) {
      return 'gecko1_8';
    }
    return 'unknown';
  }
  ;
  values['user.agent'] = {gecko1_8:0, ie6:1, ie8:2, opera:3, safari:4};
  euosmegwt.onScriptLoad = function(){
    if (frameInjected) {
      loadDone = true;
      maybeStartModule();
    }
  }
  ;
  euosmegwt.onInjectionDone = function(){
    scriptsDone = true;
    $stats && $stats({moduleName:'euosmegwt', sessionId:$sessionId_0, subSystem:'startup', evtGroup:'loadExternalRefs', millis:(new Date).getTime(), type:'end'});
    maybeStartModule();
  }
  ;
  processMetas();
  computeScriptBase();
  var strongName;
  var initialHtml;
  if (isHostedMode()) {
    if ($wnd_0.external && ($wnd_0.external.initModule && $wnd_0.external.initModule('euosmegwt'))) {
      $wnd_0.location.reload();
      return;
    }
    initialHtml = 'hosted.html?euosmegwt';
    strongName = '';
  }
  $stats && $stats({moduleName:'euosmegwt', sessionId:$sessionId_0, subSystem:'startup', evtGroup:'bootstrap', millis:(new Date).getTime(), type:'selectingPermutation'});
  if (!isHostedMode()) {
    try {
      unflattenKeylistIntoAnswers(['fi', 'ie8'], '014DF029991C1D0E1ED66F4F39432388');
      unflattenKeylistIntoAnswers(['el', 'opera'], '0461C72C235014FE5EEBD7213C2A88F0');
      unflattenKeylistIntoAnswers(['da', 'gecko1_8'], '05BF6B66AC756F3FCA28BBC6AB6377FC');
      unflattenKeylistIntoAnswers(['cs', 'ie6'], '07BDCA8E647A73934773BBC678474B11');
      unflattenKeylistIntoAnswers(['default', 'ie6'], '07C4BEDD6C9429B0AC5E5346135971EF');
      unflattenKeylistIntoAnswers(['sv', 'gecko1_8'], '0867A3A726F8CEA084F91F860F71A5AA');
      unflattenKeylistIntoAnswers(['sl', 'ie6'], '0B48E73C95D702AD6D8040306779F617');
      unflattenKeylistIntoAnswers(['sk', 'opera'], '0FA2D75A7A9D5C820A9CA765D92DC95B');
      unflattenKeylistIntoAnswers(['ro', 'ie8'], '10D51EEC41C4365117CD058550C3FDBF');
      unflattenKeylistIntoAnswers(['de', 'ie8'], '112D3D4088B31B29FBCDFBFF39969A85');
      unflattenKeylistIntoAnswers(['hu', 'ie8'], '117F95A13DE3CEBD8A14C087C06D72D5');
      unflattenKeylistIntoAnswers(['ro', 'safari'], '1357E43D68AFC14148FD3E90AFE77E47');
      unflattenKeylistIntoAnswers(['et', 'ie8'], '186A8FE5F23D606669D29D1F7C2E3C31');
      unflattenKeylistIntoAnswers(['ro', 'opera'], '18A755CF251A686EAE7CDFAFD6068AD4');
      unflattenKeylistIntoAnswers(['pt', 'ie8'], '191AA712A52A255182E887D45115B72F');
      unflattenKeylistIntoAnswers(['pl', 'ie8'], '1A5BEE645DBE181CE361BAA2EE079DFB');
      unflattenKeylistIntoAnswers(['fr', 'safari'], '1AF18F63CEAAA2CACA2FB86ED6A2BF74');
      unflattenKeylistIntoAnswers(['en', 'safari'], '20B8A61BF3112DB7C6A81B19A611E40C');
      unflattenKeylistIntoAnswers(['cs', 'ie8'], '226280E0B20E89F49ACDB16C301DF312');
      unflattenKeylistIntoAnswers(['de', 'gecko1_8'], '2ADE9606C17E14AC2B8607372F45E542');
      unflattenKeylistIntoAnswers(['mt', 'ie8'], '2BFD9C4D691FA48DCDE27DAA43BEE3DE');
      unflattenKeylistIntoAnswers(['bg', 'opera'], '2D2A708052E81A76839A0A4A54E0AC2C');
      unflattenKeylistIntoAnswers(['mt', 'opera'], '2DAE699368F6C0D694DBF4CA07259EA5');
      unflattenKeylistIntoAnswers(['default', 'ie8'], '2FD9A76F717F0E988DBA82852181270A');
      unflattenKeylistIntoAnswers(['fr', 'opera'], '3299089419EE8FB06FAC2015BD772281');
      unflattenKeylistIntoAnswers(['lt', 'ie6'], '3AE8BB03557732B02204DC802B3A3AEE');
      unflattenKeylistIntoAnswers(['fi', 'opera'], '3B5C8E85ABAA668D49A555F574A4C61D');
      unflattenKeylistIntoAnswers(['bg', 'gecko1_8'], '452FE7DDBD0EE0DF89E2333E93CA4AA7');
      unflattenKeylistIntoAnswers(['nl', 'gecko1_8'], '47BCD3489042513F04DF511ED105DDD9');
      unflattenKeylistIntoAnswers(['nl', 'ie6'], '49370259978CAE14858D35AD180E3658');
      unflattenKeylistIntoAnswers(['nl', 'ie8'], '4A505908E826839FDD85A4FACD41624E');
      unflattenKeylistIntoAnswers(['et', 'opera'], '4AD8DBB1DD7750E41A1060AED6B2C763');
      unflattenKeylistIntoAnswers(['en', 'ie6'], '4BAE3D412A36F28B09757D72BCBB802C');
      unflattenKeylistIntoAnswers(['pl', 'safari'], '50AD138D0F1DB681AB76AB8102A11244');
      unflattenKeylistIntoAnswers(['it', 'opera'], '51F3BBD9284C94D887C841B6272077D1');
      unflattenKeylistIntoAnswers(['sv', 'ie6'], '523F4547B5975F8D1AF08FF61AA0DE9A');
      unflattenKeylistIntoAnswers(['pt', 'safari'], '566FE0DA79BD4B5528355C3CAD26DC6B');
      unflattenKeylistIntoAnswers(['hu', 'safari'], '5E2598C0D6F9E84FB3470B016984C10D');
      unflattenKeylistIntoAnswers(['fr', 'gecko1_8'], '6330DF82EBB77FC7797799EA9A33F0A5');
      unflattenKeylistIntoAnswers(['el', 'gecko1_8'], '64A0778C067B17608DA74F3FF6D6B6FD');
      unflattenKeylistIntoAnswers(['cs', 'safari'], '66A25E78489E5CD6C04A204C538BB7CE');
      unflattenKeylistIntoAnswers(['es', 'opera'], '674F8EB970BF60C4BFD227E74FA659EC');
      unflattenKeylistIntoAnswers(['it', 'ie8'], '6A3B8CD3B98660D02E6D42B6BB92582F');
      unflattenKeylistIntoAnswers(['sl', 'safari'], '6A6A61FFC9099DEB6D282B6F6464BA57');
      unflattenKeylistIntoAnswers(['pt', 'gecko1_8'], '6D05A3CE9F596706593944EF2C35FD14');
      unflattenKeylistIntoAnswers(['bg', 'ie8'], '6D6E1755F96C538B7552C2EFFBD95903');
      unflattenKeylistIntoAnswers(['pt', 'ie6'], '6F6870FD538970BE3FF34C7DCA97D4DD');
      unflattenKeylistIntoAnswers(['et', 'ie6'], '728D56FA3A2457990FE2EAC5636E6A83');
      unflattenKeylistIntoAnswers(['en', 'opera'], '730322CD5D7E554155FDAD1B0275ED0B');
      unflattenKeylistIntoAnswers(['fi', 'gecko1_8'], '73E7268C7C035B373356DC136332F070');
      unflattenKeylistIntoAnswers(['da', 'opera'], '73EAE76A52FBD7236E055DCD46AFFF01');
      unflattenKeylistIntoAnswers(['sv', 'ie8'], '74DEFE745B1CBD83B4DCC024AA85E1FE');
      unflattenKeylistIntoAnswers(['sk', 'ie8'], '76305213FE5F71FE8D848E203FED25A4');
      unflattenKeylistIntoAnswers(['pl', 'ie6'], '77DC0077F0986F2D07638F148A521B8F');
      unflattenKeylistIntoAnswers(['default', 'gecko1_8'], '7A697898191109106697D9E96E5F2634');
      unflattenKeylistIntoAnswers(['lv', 'ie8'], '7C8CC7EFACE3AA6BD751B6DC9CE0B163');
      unflattenKeylistIntoAnswers(['cs', 'gecko1_8'], '7E5842F3E0DDCE3FEEE933ACC2A415A9');
      unflattenKeylistIntoAnswers(['hu', 'gecko1_8'], '7FA2E710EA1367C187560F00C1E3D280');
      unflattenKeylistIntoAnswers(['es', 'ie8'], '7FB130D81E7750A3B51E419F03BA126A');
      unflattenKeylistIntoAnswers(['it', 'safari'], '804A86766CF0FE2CD0591ABD601F4385');
      unflattenKeylistIntoAnswers(['mt', 'ie6'], '8256C085776219AB74F0ACDD0EA63433');
      unflattenKeylistIntoAnswers(['it', 'gecko1_8'], '845C9393190630B39656A15C9C88C629');
      unflattenKeylistIntoAnswers(['sv', 'safari'], '868B502995D467CD68230A960F8CC4BF');
      unflattenKeylistIntoAnswers(['fr', 'ie6'], '8D296D884E7BC375D118BF44A244ECDD');
      unflattenKeylistIntoAnswers(['es', 'safari'], '8E761532BFD481FEB76F15E49D69719F');
      unflattenKeylistIntoAnswers(['sl', 'gecko1_8'], '8F188FE524BA1FC2B6F7C3D9BFDB9E74');
      unflattenKeylistIntoAnswers(['lt', 'ie8'], '8F5F175CBF04133B3EF884BC1766BC17');
      unflattenKeylistIntoAnswers(['lv', 'safari'], '926EFB7892FDDFACD8BD313EC4691B5D');
      unflattenKeylistIntoAnswers(['mt', 'safari'], '968F7C698D1197DB6F60EB03DA97B8D4');
      unflattenKeylistIntoAnswers(['lt', 'gecko1_8'], '9712EFE9A7A8AD5DDA0E1C00FCDBF85B');
      unflattenKeylistIntoAnswers(['de', 'safari'], '98D67A2ADB5C8EBB1753159BCBAD4417');
      unflattenKeylistIntoAnswers(['it', 'ie6'], '9C84AF4CC2F1E030415285A5252A5AD0');
      unflattenKeylistIntoAnswers(['de', 'ie6'], '9CC0DE156DAE121065F3F9BA82D73DDA');
      unflattenKeylistIntoAnswers(['de', 'opera'], 'A0B448B943C155FC2674521BCB9C2A15');
      unflattenKeylistIntoAnswers(['fi', 'safari'], 'A2070D47F4EF06FCC30776AF0568BE22');
      unflattenKeylistIntoAnswers(['default', 'opera'], 'A39678DFBFD0FDACC7477CD6E3BE5CED');
      unflattenKeylistIntoAnswers(['pl', 'opera'], 'A477961AE07C029DC2C4D392C44FFB23');
      unflattenKeylistIntoAnswers(['bg', 'safari'], 'A67AB1F116EA6BF9DCD28796D50E3487');
      unflattenKeylistIntoAnswers(['hu', 'opera'], 'B18AE43AAE1F1EFB6216A868DDAD8C44');
      unflattenKeylistIntoAnswers(['el', 'ie8'], 'B3FB9DC99DA386497143E49BC5787C7D');
      unflattenKeylistIntoAnswers(['da', 'ie6'], 'B7365085CC4532DA30AB38CFC351B650');
      unflattenKeylistIntoAnswers(['el', 'ie6'], 'B76B7BED2319E986A7BF416F7CC3F1D9');
      unflattenKeylistIntoAnswers(['sk', 'ie6'], 'BB617777CD47A767F1F144C3AB63E27E');
      unflattenKeylistIntoAnswers(['lv', 'opera'], 'BD9908263B75D8D8FAD8AC2BCFC37A24');
      unflattenKeylistIntoAnswers(['et', 'gecko1_8'], 'BF0AF6F610FBCF811CA4DF5041649625');
      unflattenKeylistIntoAnswers(['lt', 'opera'], 'BF9B322B3A628B38B5BB3CEE2BC79870');
      unflattenKeylistIntoAnswers(['et', 'safari'], 'C0CF95B2E5CE703A9B702CAD7552C411');
      unflattenKeylistIntoAnswers(['sk', 'gecko1_8'], 'C2B3907AFD31EBEB77556A01443B3801');
      unflattenKeylistIntoAnswers(['da', 'safari'], 'C69AE09A0AC162E648C4C94ACE5D6AFA');
      unflattenKeylistIntoAnswers(['da', 'ie8'], 'C73752053A92F2A096FBB59C20FED794');
      unflattenKeylistIntoAnswers(['bg', 'ie6'], 'CF942B6849E522DBE693A7556ECF1ED1');
      unflattenKeylistIntoAnswers(['sk', 'safari'], 'CF98D9FF091D9A893B453873BBD3F33A');
      unflattenKeylistIntoAnswers(['hu', 'ie6'], 'D174241F8A74F60D655643F7794AA5CF');
      unflattenKeylistIntoAnswers(['nl', 'safari'], 'D27C33EABB80DB63E3B24F42183E75E4');
      unflattenKeylistIntoAnswers(['ro', 'ie6'], 'D2B0C6CD7519F8122C8467154F4C89C3');
      unflattenKeylistIntoAnswers(['sl', 'opera'], 'D406789E6ED06363122F16C6101E2238');
      unflattenKeylistIntoAnswers(['cs', 'opera'], 'D5DB8FE9329A83750C4B023B8B3CC8C5');
      unflattenKeylistIntoAnswers(['sv', 'opera'], 'D631C322B42403A7F78BA724503822DF');
      unflattenKeylistIntoAnswers(['fr', 'ie8'], 'D6B2A5A644D0472ABA6AD8BD6509FA59');
      unflattenKeylistIntoAnswers(['nl', 'opera'], 'DE8FD025BE06256703E2626FAA51121C');
      unflattenKeylistIntoAnswers(['es', 'ie6'], 'DF314BB9AC05395E63D38CF093F80EDD');
      unflattenKeylistIntoAnswers(['ro', 'gecko1_8'], 'E61D00E511065B63D93CCE79310E86C6');
      unflattenKeylistIntoAnswers(['lt', 'safari'], 'F17AA3ECB470D3270006111F7A007347');
      unflattenKeylistIntoAnswers(['default', 'safari'], 'F3FC847DA6E52249BC1778845EECB8C8');
      unflattenKeylistIntoAnswers(['sl', 'ie8'], 'F5C567256C5BD2DF3C8AB94C772240D7');
      unflattenKeylistIntoAnswers(['pl', 'gecko1_8'], 'F741B34E4D2E7D763004CAB8178E85C2');
      unflattenKeylistIntoAnswers(['fi', 'ie6'], 'F7C117710EAE105E0DB408CFC3DC5B41');
      unflattenKeylistIntoAnswers(['en', 'ie8'], 'F8A1D14539964FC7DD7E7D58F0EA5063');
      unflattenKeylistIntoAnswers(['lv', 'gecko1_8'], 'F8E0E4F014A0687DD3DCD1EF685DA217');
      unflattenKeylistIntoAnswers(['el', 'safari'], 'F902EBFE4E2C017A40F3FF632EC78FAB');
      unflattenKeylistIntoAnswers(['en', 'gecko1_8'], 'FBC4D008173DCC2BAEF0936A3DF414EB');
      unflattenKeylistIntoAnswers(['mt', 'gecko1_8'], 'FC22439DA3596E6D6B623D5C103BB749');
      unflattenKeylistIntoAnswers(['es', 'gecko1_8'], 'FC76A354C7C72198F80A0DFE6E27FBC0');
      unflattenKeylistIntoAnswers(['lv', 'ie6'], 'FF37807278EF7363463364EAF65AEA70');
      unflattenKeylistIntoAnswers(['pt', 'opera'], 'FF7EC4DD2CF9F4B153C4F1D27D4B5BD1');
      strongName = answers[computePropValue('locale')][computePropValue('user.agent')];
      var idx = strongName.indexOf(':');
      if (idx != -1) {
        softPermutationId = Number(strongName.substring(idx + 1));
        strongName = strongName.substring(0, idx);
      }
      initialHtml = strongName + '.cache.html';
    }
     catch (e) {
      return;
    }
  }
  var onBodyDoneTimerId;
  function onBodyDone(){
    if (!bodyDone) {
      bodyDone = true;
      maybeStartModule();
      if ($doc_0.removeEventListener) {
        $doc_0.removeEventListener('DOMContentLoaded', onBodyDone, false);
      }
      if (onBodyDoneTimerId) {
        clearInterval(onBodyDoneTimerId);
      }
    }
  }

  if ($doc_0.addEventListener) {
    $doc_0.addEventListener('DOMContentLoaded', function(){
      maybeInjectFrame();
      onBodyDone();
    }
    , false);
  }
  var onBodyDoneTimerId = setInterval(function(){
    if (/loaded|complete/.test($doc_0.readyState)) {
      maybeInjectFrame();
      onBodyDone();
    }
  }
  , 50);
  $stats && $stats({moduleName:'euosmegwt', sessionId:$sessionId_0, subSystem:'startup', evtGroup:'bootstrap', millis:(new Date).getTime(), type:'end'});
  $stats && $stats({moduleName:'euosmegwt', sessionId:$sessionId_0, subSystem:'startup', evtGroup:'loadExternalRefs', millis:(new Date).getTime(), type:'begin'});
  $doc_0.write('<script defer="defer">euosmegwt.onInjectionDone(\'euosmegwt\')<\/script>');
}

euosmegwt();
