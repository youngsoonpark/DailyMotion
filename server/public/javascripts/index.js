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

    var PageManager = (function() {
      var MAX_SHOWABLE_IMAGE_NUM = 8;

      var PageManager = function(json) {
        this.imageJsonArray = json;
        this.currentPage = 1;
        init(this.imageJsonArray);
      }

      var init = function(jsonArray) {
        this.gifElementArray = [];
        for (var i = 0; i < jsonArray.length; i++) {
          var elem = createGifElement(jsonArray[i]);
          gifElementArray.push(elem);
        }
      }

      var createGifElement = function(json) {
        var gifBox = $('<div class="gif_box">');

        var createGifBoxHeader = function(title) {
          var gifBoxHeader = $('<div class="gif_box_info">');
          gifBoxHeader.append(
            '<img class="gif_box_icon" src="/images/icon.jpeg">'
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
          gifBoxFeedback.append('<img class="gif_box_comment" src="/images/comment.png">');

          var likeButton = likeCount > 0 ?
            $('<img class="gif_box_like" src="/images/like_on.png">') :
            $('<img class="gif_box_like" src="/images/like.png">');
          likeButton.click(function() {
            $.ajax({
              type: "POST",
              url: "/api/like",
              data: {id: id}
            });

            likeButton.attr("src", "/images/like_on.png");
            callDeviceMethod({
              method: "feedback.like",
              body: { title: title }
            });
          });
          gifBoxFeedback.append(likeButton);

          var downloadButton = $('<img class="gif_box_download" src="/images/download.png">');
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

      PageManager.prototype.setOnPageChangedCallback = function(callback) {
        this.pageChangedCallback = callback;
      }

      PageManager.prototype.getCurrentPage = function() {
        return this.currentPage;
      }

      PageManager.prototype.getLastPage = function() {
        return gifElementArray == null ?
          1 :
          Math.ceil(gifElementArray.length / MAX_SHOWABLE_IMAGE_NUM);
      }

      PageManager.prototype.show = function(page) {
        if (page == parseInt(page)) {
          this.currentPage = page;
          $("#content").empty();
          var offset = (page - 1) * MAX_SHOWABLE_IMAGE_NUM;
          for (var i = offset; i < offset + MAX_SHOWABLE_IMAGE_NUM; i++) {
            $("#content").append(gifElementArray[i]);
          }

          if (this.pageChangedCallback != null) {
            this.pageChangedCallback();
          }
        } else if (typeof page.valueOf() == "string"){
          if (page == "prev" && this.currentPage > 1) {
            this.show(this.currentPage - 1);
          } else if (page == "next" &&
              gifElementArray[this.currentPage * MAX_SHOWABLE_IMAGE_NUM]) {
            this.show(this.currentPage + 1);
          }
        }
      }

      return PageManager;
    })();

    var pageManager = {};
    var onReceiveImageJson = function(jsonArray) {
      pageManager = new PageManager(jsonArray);
      pageManager.setOnPageChangedCallback(function() {
        window.scrollTo(0, 0);
        $("#pager_navi").html(
          "(" +
          pageManager.getCurrentPage() +
          "/" + 
          pageManager.getLastPage() +
          ")");
      });
      pageManager.show(1);
    }

    $("#pager_button_prev").click(function() {
      pageManager.show("prev");
    });
    $("#pager_button_next").click(function() {
      pageManager.show("next");
    });

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
    }

  });
}).call(this);
