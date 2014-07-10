function cropImageUploaderInit(minX, minY, maxX, maxY, cropFunc) {
    var jcrop_api;
    var croppingId = "forCropping";
    var cropContainerId = "cropContainer";
    var cropButtonId = "cropBtn";
    var canvasId = "imgOutput";
    var canvasContainerId = "hiddenResult";
    var errorContainerId = "errorContainer";
    
    // get image from file input
    function readURL(input) {
        if (input.files && input.files[0]) {
            // Only process image files.
            if ( !input.files[0].type.match('image.*')) {
            	showUploadErrorMessage('Please, use valid image file');
                return;
            }
            var reader = new FileReader();
            reader.onload = function (e) {
                // original image
                var uploadedImage = new Image();
                uploadedImage.src = e.target.result;
                uploadedImage.onload = function() {
                    // check if original image more than minimum
                    if (uploadedImage.width < minX || uploadedImage.height < minY ) {
                    	showUploadErrorMessage('Chosen image is too small, please, use a picture with dimension ' + minX + ':' + minY + ' (width:height) at least');
                		return;
                    }
                    var $cropImg = $('#'+croppingId);
                	$cropImg.attr('src', e.target.result).show();
                    // attach jCrop
                    var dim = {
                        minWidth: minX, minHeight: minY, maxWidth: maxX, maxHeight: maxY,
                        target: $cropImg,
                        origImage: uploadedImage
                    };
                    attachJcrop(dim);
                }
            }
            reader.readAsDataURL(input.files[0]);
        }
    }
    
    function showUploadErrorMessage(msg) {
    	$('#'+errorContainerId).text(msg).show();
    }
    
    $("#imgSource").change(function(){
        $("#"+cropContainerId).empty();
        $('#'+cropButtonId).hide();
        $('#'+errorContainerId).hide();
        $("#"+cropContainerId).append('<img id="' + croppingId + '" alt="your image" style="display: none; width: 100%; height: auto;"/>');
        if ( jcrop_api ) {
            jcrop_api.destroy();
        }
        readURL(this);
    });
    
    function attachJcrop(dim) {
        var origWidth = dim.origImage.width;
        var origHeight = dim.origImage.height;
        var $imgTarget = dim.target;
        var targetWidth = dim.target.width();
        var targetHeight = dim.target.height();
        var multX = origWidth/targetWidth;
        var multY = origHeight/targetHeight;

        // calc min sizes for jCrop
        var minSizeX = Math.round(dim.minWidth / multX);
        var minSizeY = Math.round(dim.minHeight / multY);
        // size of selection (near middle, centered)
        var selectW = dim.minWidth * targetHeight / dim.minHeight;
        if ( selectW > targetWidth ) {
            selectW = targetWidth;
        }
        var selectH = dim.minHeight * targetWidth / dim.minWidth;
        if ( selectH > targetHeight ) {
            selectH = targetHeight
        }
        selectW = Math.round((selectW + minSizeX) / 2);
        selectH = Math.round((selectH + minSizeY) / 2);
        var selectLeft = Math.round((targetWidth-selectW)/2);
        var selectTop = Math.round((targetHeight-selectH)/2);

        var coords = {};

        $imgTarget.Jcrop({
            aspectRatio: dim.minWidth / dim.minHeight,
            setSelect: [ selectLeft, selectTop, selectLeft + selectW, selectTop + selectH ],
            onChange:   showCoords,
            onSelect:   showCoords,
            allowSelect: false,
            allowMove: true,
            allowRisize: true,
            minSize: [minSizeX, minSizeY]
        },function(){
            jcrop_api = this;
            $('#'+cropButtonId).show();
        });

        function showCoords(c) {
            coords = c;
        }
		
		$('#'+cropButtonId).unbind('click');
        $('#'+cropButtonId).click(function() {
            // crop original image
            var cW = Math.round(coords.w * multX); 
            var cH = Math.round(coords.h * multY);
            // less than max dimension
            var imgW = ( cW < dim.maxWidth ) ? cW : dim.maxWidth;
            var imgH = ( cH < dim.maxHeight ) ? cH : dim.maxHeight;
            var sx = Math.round(coords.x * multX);
            var sy = Math.round(coords.y * multY);
            $('canvas').attr('width', imgW).attr('height', imgH);
            
            // direct canvas
            var canvas = document.getElementById(canvasId);
            var ctx = canvas.getContext('2d');
            ctx.drawImage(dim.origImage, sx, sy, cW, cH, 0, 0, imgW, imgH);
            //$('#'+canvasContainerId).show();
            
            // set body image
            var dataUrlPattern = /data:([\w]+)\/([\w]+);([\w]+),(.+)/i;
            var dataUrl = canvas.toDataURL('image/jpeg', 0.85);
            if ( dataUrl.match(dataUrlPattern) ) {
            	cropFunc(RegExp.$4);
            }
        });
    }
}