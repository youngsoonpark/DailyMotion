(function() {
  $(document).ready(function() {
    $(".gif_box_download").each(function() {
      $(this).click(function() {
        var image_url = ($(this).attr("src"));
        callDeviceMethod({
          method: "download.image",
          body: image_url
        });
      });
    });

    callDeviceMethod = function(json) {
      try {
        Device.call(JSON.stringify(json));
      } catch (e) {
        console.log("本来であればアプリ内ブラウザでみるもの: " + e);
      }
    };
  });
}).call(this);
