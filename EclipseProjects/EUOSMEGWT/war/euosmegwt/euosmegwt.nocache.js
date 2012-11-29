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
      unflattenKeylistIntoAnswers(['es', 'safari'], '044D40E31DCBB298EF918BCD728215BB');
      unflattenKeylistIntoAnswers(['cs', 'ie8'], '05FD77AF9172FB19D6C5787866A58F06');
      unflattenKeylistIntoAnswers(['sl', 'ie8'], '0B206E194932C232411CA75F80B8B06C');
      unflattenKeylistIntoAnswers(['fr', 'ie8'], '0DD6518D8062D055A0482E73E908C3C1');
      unflattenKeylistIntoAnswers(['ro', 'ie8'], '116899BBF0D01D1D4E4D091D113B3D11');
      unflattenKeylistIntoAnswers(['cs', 'opera'], '13D07DC1828739D813C39A1FF51133DB');
      unflattenKeylistIntoAnswers(['default', 'ie6'], '15A2334E51FFF0D86D16B8876A56B4F5');
      unflattenKeylistIntoAnswers(['hu', 'safari'], '16577C763CBDED5F7BB24E60B461DC53');
      unflattenKeylistIntoAnswers(['et', 'ie6'], '17866E0AADD88D73B92634791FE953FD');
      unflattenKeylistIntoAnswers(['et', 'gecko1_8'], '18CF47AD2963E32270A0C195BCFE0CA9');
      unflattenKeylistIntoAnswers(['el', 'ie8'], '1ABF091CE71DC2E075E25C5DC4233B79');
      unflattenKeylistIntoAnswers(['hu', 'opera'], '1B7BBADA78E02A4BCBFA2F83D8D12CF3');
      unflattenKeylistIntoAnswers(['da', 'ie8'], '1D498DF65867D94654032073FC668D31');
      unflattenKeylistIntoAnswers(['fi', 'ie6'], '209A65FDC8A721A594EBF0DE781954C5');
      unflattenKeylistIntoAnswers(['pt', 'opera'], '213F4F09311D96B183EAD24D1E43D2FA');
      unflattenKeylistIntoAnswers(['lt', 'safari'], '22002FF326A516F5D7EAEB7127F86A7A');
      unflattenKeylistIntoAnswers(['lt', 'gecko1_8'], '239CB3E998591E6CDA27C732A3EB84EF');
      unflattenKeylistIntoAnswers(['sk', 'safari'], '23BB1D868AC13FF7E3EB70B1EA8B8FF3');
      unflattenKeylistIntoAnswers(['nl', 'ie8'], '265DC8F904378B8402A339D199B48B03');
      unflattenKeylistIntoAnswers(['sk', 'opera'], '286F24556B4600B2A53E883C09C30D22');
      unflattenKeylistIntoAnswers(['sv', 'gecko1_8'], '286F7DCB0706D8B67B89386E5EBA5060');
      unflattenKeylistIntoAnswers(['es', 'gecko1_8'], '28927115FB29A7C7887D94DEAF9D15CA');
      unflattenKeylistIntoAnswers(['sk', 'ie6'], '2A3D3DA2E2AAFE6687BB41286F4EC0B1');
      unflattenKeylistIntoAnswers(['en', 'ie8'], '2E44F3709061957DB3B61E8D36BA0DFB');
      unflattenKeylistIntoAnswers(['bg', 'ie8'], '2FA9AA1FBC2DC6F8DA9DCDF1924B0C34');
      unflattenKeylistIntoAnswers(['sl', 'opera'], '31301B2F6CC75BB10D5EBB12F87A8E46');
      unflattenKeylistIntoAnswers(['mt', 'gecko1_8'], '349815E788C8CC13022AA0E159965656');
      unflattenKeylistIntoAnswers(['it', 'ie6'], '3990F7EFA831A8FE9E00B6325517BBEB');
      unflattenKeylistIntoAnswers(['da', 'ie6'], '3A143CCADF67ED9AFA6B390416E8DB7C');
      unflattenKeylistIntoAnswers(['cs', 'gecko1_8'], '3B7FDC23F19FB80B7AF67141EBE61468');
      unflattenKeylistIntoAnswers(['lv', 'safari'], '3E7DE557FA8B77A914BCBE247752480B');
      unflattenKeylistIntoAnswers(['sl', 'safari'], '3EDBD13D114B0E0767CB3F778C876637');
      unflattenKeylistIntoAnswers(['pt', 'ie8'], '43E3AC0C0208333EEC64F8DFAA80FD8E');
      unflattenKeylistIntoAnswers(['da', 'safari'], '47AEFAD84C5BC842B45D5EE9882A642D');
      unflattenKeylistIntoAnswers(['mt', 'ie8'], '4BF3EF0E00FEE45518E9CD9B5B346C48');
      unflattenKeylistIntoAnswers(['fi', 'ie8'], '4CD5EFC3ABE5331B282C9443ACDEEBB9');
      unflattenKeylistIntoAnswers(['da', 'opera'], '53069B80AD5771617413BF0080D56010');
      unflattenKeylistIntoAnswers(['it', 'opera'], '551AE626BD1A42707AAD65136675FC14');
      unflattenKeylistIntoAnswers(['default', 'safari'], '563AED0A90FFA07F03C3F355F73EED81');
      unflattenKeylistIntoAnswers(['et', 'opera'], '567703418233031C9574FF108B9E55BD');
      unflattenKeylistIntoAnswers(['pl', 'safari'], '5720FD7D516F7A8078F2A574F9E30475');
      unflattenKeylistIntoAnswers(['es', 'opera'], '57428EBA97CF86C0DE91A6541EC52467');
      unflattenKeylistIntoAnswers(['de', 'ie8'], '57670FE524FA7DAE3029C67301C1B36D');
      unflattenKeylistIntoAnswers(['ro', 'gecko1_8'], '5E460DD7CD35C5E16081EAE28603B89A');
      unflattenKeylistIntoAnswers(['es', 'ie8'], '5F2A86DD2B00BBE9EAD2ECC1D42058BD');
      unflattenKeylistIntoAnswers(['sv', 'safari'], '65C7D509F9C4AA625FB33BCD9F89847F');
      unflattenKeylistIntoAnswers(['pt', 'gecko1_8'], '6A8F94E930F5D66682CE3E012B038F82');
      unflattenKeylistIntoAnswers(['pt', 'ie6'], '6D68B70BB5810BAB16198143070DC351');
      unflattenKeylistIntoAnswers(['fr', 'opera'], '6EBD9C5F0CC4FFA909B7408AAA52AF82');
      unflattenKeylistIntoAnswers(['en', 'safari'], '6F710B71DEE7C22FAEB9C6F4D5890D84');
      unflattenKeylistIntoAnswers(['el', 'safari'], '70167E841F7333D55BEF0BF8AB36E25C');
      unflattenKeylistIntoAnswers(['it', 'safari'], '72A7F5AAF97E6E671E8FDFA322E73FE5');
      unflattenKeylistIntoAnswers(['sk', 'ie8'], '7356EEE8836FC56C25AEB0DAE9A173E2');
      unflattenKeylistIntoAnswers(['ro', 'ie6'], '7752BBB454B343DB595C44FE7DC4CCCB');
      unflattenKeylistIntoAnswers(['sk', 'gecko1_8'], '78FA51298151592819EBC35E8DBD9003');
      unflattenKeylistIntoAnswers(['hu', 'ie6'], '7C118190E10704A9D52FA73621DF9C9A');
      unflattenKeylistIntoAnswers(['fr', 'safari'], '7D6151389013DC08A0600CCBD0BCF3F3');
      unflattenKeylistIntoAnswers(['nl', 'safari'], '804DD1503BB901AA2E8896EC3531E92F');
      unflattenKeylistIntoAnswers(['lt', 'opera'], '81919267C9BD2F9D340DC7463B6781D7');
      unflattenKeylistIntoAnswers(['el', 'gecko1_8'], '839CC76A093C6FA25599C2817D9467B9');
      unflattenKeylistIntoAnswers(['sv', 'opera'], '844751CFE60A800E2925927FFA3F3274');
      unflattenKeylistIntoAnswers(['de', 'gecko1_8'], '845258ADE7B7C331F605002E4EE76D70');
      unflattenKeylistIntoAnswers(['lv', 'ie8'], '84AC7664B9D71C750FBCA1F209E0DD24');
      unflattenKeylistIntoAnswers(['sv', 'ie8'], '86D2F9E3C038027555B994CA780328A1');
      unflattenKeylistIntoAnswers(['ro', 'opera'], '875846210B58D1926A06A12BEDDB0CE8');
      unflattenKeylistIntoAnswers(['da', 'gecko1_8'], '8A0DB7F2E3D42406CD187C63E3B409D6');
      unflattenKeylistIntoAnswers(['fr', 'ie6'], '8A2EDCF9C3CA1C455F1AA832E370B5BF');
      unflattenKeylistIntoAnswers(['de', 'ie6'], '8DFA820CD29FC1EC3A8915852029899D');
      unflattenKeylistIntoAnswers(['pt', 'safari'], '8E071E62AAD2C8951469552D450E224A');
      unflattenKeylistIntoAnswers(['default', 'gecko1_8'], '8E5A7E21246AE3140B70266304D7E80E');
      unflattenKeylistIntoAnswers(['hu', 'gecko1_8'], '906F50E675370069656D403F2BDA10A0');
      unflattenKeylistIntoAnswers(['lv', 'gecko1_8'], '917E40DBB631DE7E732F1700466F7A3B');
      unflattenKeylistIntoAnswers(['nl', 'ie6'], '92A5C4C870FB1B4CA242835F5102D12E');
      unflattenKeylistIntoAnswers(['en', 'gecko1_8'], '93A67FF0D925F54706FCE0B820C161A1');
      unflattenKeylistIntoAnswers(['de', 'safari'], '94A9C8758C397D0B312D4F48F0459BE4');
      unflattenKeylistIntoAnswers(['cs', 'ie6'], '95DE1734A00E22269A47A9FF3DDACB00');
      unflattenKeylistIntoAnswers(['default', 'ie8'], '974995544C1984C29F67308635C35B4C');
      unflattenKeylistIntoAnswers(['mt', 'opera'], '97EA5888C8C16D99428687C99A29F0D0');
      unflattenKeylistIntoAnswers(['en', 'ie6'], '981BBA411EB30C461F8310503600AA80');
      unflattenKeylistIntoAnswers(['nl', 'gecko1_8'], '985DC2611D60BA71D1986BA1639EB102');
      unflattenKeylistIntoAnswers(['lv', 'ie6'], '993AAC8FB537AEAF386AF92573D33B9A');
      unflattenKeylistIntoAnswers(['de', 'opera'], '99BD324383708A44850C80870008C39A');
      unflattenKeylistIntoAnswers(['hu', 'ie8'], '9B87696D9B70DF1F60E9237261075C5C');
      unflattenKeylistIntoAnswers(['nl', 'opera'], '9D01B45954512523CCE0453AEFEF5F93');
      unflattenKeylistIntoAnswers(['el', 'opera'], '9F44AFBF69F0653A8F697B7C98E3A59F');
      unflattenKeylistIntoAnswers(['es', 'ie6'], 'A3A33A9C4BBBBD099E6D79ADF4048F12');
      unflattenKeylistIntoAnswers(['sv', 'ie6'], 'A4DCAC4BE460A84B9D899C980B8413D3');
      unflattenKeylistIntoAnswers(['cs', 'safari'], 'A6045127F1EC05F73413B4415A73566C');
      unflattenKeylistIntoAnswers(['et', 'ie8'], 'A7F7DB2A5776D50A32D5128103076DE6');
      unflattenKeylistIntoAnswers(['bg', 'ie6'], 'B080AEE9233554A75525CF150E3E69F7');
      unflattenKeylistIntoAnswers(['it', 'gecko1_8'], 'B671EC7758530447553301FCEF186122');
      unflattenKeylistIntoAnswers(['el', 'ie6'], 'B6ADEE5F23992764B2DBF9A5C1CEF4C0');
      unflattenKeylistIntoAnswers(['en', 'opera'], 'B6FC6FE7F139700F7FBF823135624086');
      unflattenKeylistIntoAnswers(['pl', 'ie6'], 'B856A8235472C3519095CA0C0538E330');
      unflattenKeylistIntoAnswers(['ro', 'safari'], 'BA08021E3856C68AB005BB8FFB4A90CB');
      unflattenKeylistIntoAnswers(['it', 'ie8'], 'BA2FB778F4231981E40FA6C1B1DB96D2');
      unflattenKeylistIntoAnswers(['bg', 'safari'], 'BA4513E4005B92489874C786CBE37C92');
      unflattenKeylistIntoAnswers(['pl', 'opera'], 'BC16CAA2D435DA8F77CE305705568DA9');
      unflattenKeylistIntoAnswers(['default', 'opera'], 'C0DEB0C89CDF5B5BE2E4CE812F0E5249');
      unflattenKeylistIntoAnswers(['et', 'safari'], 'C2E34C35A9CA8D906129ECC209AFE954');
      unflattenKeylistIntoAnswers(['fi', 'safari'], 'CEE2EE402DCAD9CB63BAAA542C0EEFD9');
      unflattenKeylistIntoAnswers(['lv', 'opera'], 'D14D3C74EEAFD926B9CC519CC78D44B8');
      unflattenKeylistIntoAnswers(['pl', 'ie8'], 'D1EC921E5D4778A81FF3A35097F3DB20');
      unflattenKeylistIntoAnswers(['fr', 'gecko1_8'], 'D7901645412F712D1EF3277F2FD89532');
      unflattenKeylistIntoAnswers(['fi', 'opera'], 'E1B4775B9BA30B4E6DCDE7AAC36775EC');
      unflattenKeylistIntoAnswers(['pl', 'gecko1_8'], 'E27A3DE0FF46370390628FF7DAFBBD20');
      unflattenKeylistIntoAnswers(['fi', 'gecko1_8'], 'E5A661B98B3F962E289BF36F030E45CD');
      unflattenKeylistIntoAnswers(['sl', 'gecko1_8'], 'E98763D0A9C8008B8D92AA359892C6A5');
      unflattenKeylistIntoAnswers(['mt', 'ie6'], 'ED065614D0D089EDE6BAB6F96A87FF0D');
      unflattenKeylistIntoAnswers(['lt', 'ie8'], 'ED54D14A2DA2BCF9F60080F0A6C8C37B');
      unflattenKeylistIntoAnswers(['bg', 'opera'], 'F139108E53B1C80A01221AD0D3A71EC5');
      unflattenKeylistIntoAnswers(['sl', 'ie6'], 'F8B30DFB0B94AB35E95BAD53C8D3FFEB');
      unflattenKeylistIntoAnswers(['bg', 'gecko1_8'], 'FAA638741274A4EA018EA576C63FAF17');
      unflattenKeylistIntoAnswers(['lt', 'ie6'], 'FC5FB799BC9BBC0876D6A255069DF17F');
      unflattenKeylistIntoAnswers(['mt', 'safari'], 'FD8562259493F89181D9895756BCA189');
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
  if (!__gwt_scriptsLoaded['js/gwt-openlayers/util.js']) {
    __gwt_scriptsLoaded['js/gwt-openlayers/util.js'] = true;
    document.write('<script language="javascript" src="' + base + 'js/gwt-openlayers/util.js"><\/script>');
  }
  $doc_0.write('<script defer="defer">euosmegwt.onInjectionDone(\'euosmegwt\')<\/script>');
}

euosmegwt();
