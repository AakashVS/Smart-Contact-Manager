console.log("admin user");

document
    .querySelector("#image_file_input")
    .addEventListener("change", function (event) {
        var file = event.target.files[0];  
        var reader = new FileReader();
        reader.onload = function () {
            document
                .querySelector('#upload_image')  
                .setAttribute("src", reader.result);
        };
        if (file) {
            reader.readAsDataURL(file);  // Start reading the file as a Data URL
        }
    });

    
