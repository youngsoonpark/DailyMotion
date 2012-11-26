(function() {
  window.onload = function() {

    onImageClick = function() {
      callDeviceMethod({
        method: "download.image",
        body: "url"
      });
    }

    callDeviceMethod = function(json) {
      Device.call(JSON.stringify(json));
    };
  }
}).call(this);
