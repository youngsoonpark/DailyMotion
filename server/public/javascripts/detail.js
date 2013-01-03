(function() {
  $(document).ready(function() {

    var CENTER_CROP_SIZE = 590;
    var centerCrop = function(image, desiredSize) {
      image.onload = function() {
        var width = image.width;
        var height = image.height;

        var widthScaleRatio = desiredSize / width;
        var heightScaleRatio = desiredSize / height;
        var scaleRatio = Math.min(widthScaleRatio, heightScaleRatio);

        image.width = width * scaleRatio;
        image.height = height * scaleRatio;
      }
    }
    centerCrop($(".gif_image")[0], CENTER_CROP_SIZE);

    var onReceiveImageJson = function(json) {
      var id = json["id"];
      var title = json["title"];
      var likeCount = json["like_count"];

      $(".gif_box_like").click(function() {
        $(".gif_box_like").attr("src", "/images/like_on.png");
        $.ajax({
          type: "POST",
          url: "/api/like",
          data: {id: id}
        });

        callDeviceMethod({
          method: "feedback.like",
          body: { title: title }
        });
      });

      $(".gif_box_download").click(function() {
        callDeviceMethod({
          method: "download.image",
          body: { image_url: imageUrl }
        });
      });
    }

    var pathArray = location.pathname.split("/");
    var id = pathArray[pathArray.length - 1];
    $.ajax({
      type: "GET",
      url: "/api/get/image?id=" + id,
      dataType: "json", 
      success: function(data, textStatus, jqXHR) {
        onReceiveImageJson(data);
      },
      error: function(jqXHR, textStatus, errorThrown) {
        console.log("Something is wrong...");
      }
    });

    var callDeviceMethod = function(json) {
      try {
        Device.call(JSON.stringify(json));
      } catch (e) {
        console.log("本来であればアプリ内ブラウザでみるもの: " + e);
      }
    }

  });
}).call(this);
