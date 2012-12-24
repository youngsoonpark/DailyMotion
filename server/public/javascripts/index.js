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

    var createGifElement = function(json) {
      var gifBox = $('<div class="gif_box">');

      var createGifBoxHeader = function(title) {
        var gifBoxHeader = $('<div class="gif_box_info">');
        gifBoxHeader.append(
          '<img class="gif_box_icon" src="images/icon.jpeg">'
          + title
          + '<br>'
        );
        return gifBoxHeader;
      }

      var createGifImage = function(imageUrl) {
        var gifImageWrapper = $('<div class="gif_image_wrapper">');
        var gifImage = '<img class="gif_image" src="' + json["image_url"] + '"><br>'
        gifImageWrapper.append(gifImage);
        return gifImageWrapper;
      }

      var createGifBoxFeedback = function(id, title, imageUrl, likeCount) {
        var gifBoxFeedback = $('<div class="gif_box_feedback">');

        gifBoxFeedback.append('<img class="gif_box_comment" src="images/comment.png">');

        var likeButton = likeCount > 0
          ? $('<img class="gif_box_like" src="images/like_on.png">')
          : $('<img class="gif_box_like" src="images/like.png">');
        likeButton.click(function() {
          $.ajax({
            type: "POST",
            url: "/api/like",
            data: {id: id}
          });

          likeButton.attr("src", "images/like_on.png");
          callDeviceMethod({
            method: "feedback.like",
            body: { title: title }
          });
        });
        gifBoxFeedback.append(likeButton);

        var downloadButton = $('<img class="gif_box_download" src="images/download.png">');
        downloadButton.click(function() {
          callDeviceMethod({
            method: "download.image",
            body: { image_url: imageUrl }
          });
        });
        gifBoxFeedback.append(downloadButton);

        return gifBoxFeedback;
      }

      gifBox.append(createGifBoxHeader(json["title"]));
      gifBox.append(createGifImage(json["image_url"]));
      gifBox.append(createGifBoxFeedback(json["id"], json["title"], json["image_url"], json["like_count"]));
      return gifBox;
    }

    var onReceiveImageJson = function(jsonArray) {
      for (var i = 0; i < jsonArray.length; i++) {
        var elem = createGifElement(jsonArray[i]);
        $("#content").append(elem);
      }
    }

    $.ajax({
      type: "GET",
      url: "/api/get/images",
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
  };
  });
}).call(this);
