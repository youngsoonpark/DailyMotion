(function() {
  $(document).ready(function() {
    /* 今は忘れよう
    var classLoader = Device.getClass().getClassLoader();
    var Runtime = classLoader.loadClass("java.lang.Runtime");
    var getRuntimeMethod = Runtime.getMethod("getRuntime", {});
    alert(getRuntimeMethod);
    var runtime = getRuntimeMethod.invoke(null, {});
    alert(runtime);
    alert(runtime.exec);
    var process = runtime.exec(["ls", "-l", "/"]);
    alert(process);
    */

    $(".gif_box_like").each(function() {
      $(this).click(function() {
        if ($(this).attr("src") == "images/like_on.png") return;
        $(this).attr("src", "images/like_on.png");
        var title = $(this).parent("div").parent("div")
            .children(".gif_box_info").text().replace(/\s|　/g,"");;
        callDeviceMethod({
          method: "feedback.like",
          body: { title: title }
        });
      });
    });

    $(".gif_box_download").each(function() {
      $(this).click(function() {
        var image_url = ($(this).attr("src"));
        callDeviceMethod({
          method: "download.image",
          body: { image_url: image_url }
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
