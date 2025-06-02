window.onload = function() {
  //<editor-fold desc="Changeable Configuration Block">

  const docExpansion = localStorage.getItem(docExpansionKey) || 'list';
  const defaultModelRendering = localStorage.getItem(defaultModelRenderingKey) || 'example';

  // the following lines will be replaced by docker/configurator, when it runs in a docker-container
  window.ui = SwaggerUIBundle({
    url: "https://petstore.swagger.io/v2/swagger.json",
    dom_id: '#swagger-ui',
    deepLinking: true,
    presets: [
      SwaggerUIBundle.presets.apis,
      SwaggerUIStandalonePreset
    ],
    plugins: [
      SwaggerUIBundle.plugins.DownloadUrl
    ],
    layout: "StandaloneLayout" ,
    "configUrl" : "/v3/api-docs/swagger-config",
    "validatorUrl" : "",

    // swagger ui 설정 (하단 링크 참고)
    // https://swagger.io/docs/open-source-tools/swagger-ui/usage/configuration/
    onComplete : () => {
      setDiscription();
    },
    defaultModelExpandDepth: 10,
    persistAuthorization: true,
    docExpansion: docExpansion,
    defaultModelRendering: defaultModelRendering,
    tagsSorter: (a, b) => a.localeCompare(b),
    operationsSorter: 'method'
  });

  //</editor-fold>
};