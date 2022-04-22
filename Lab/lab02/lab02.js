$(document).ready(function() {
    var max_fields = 10;
    var wrapper = $(".hobby_wrapper");
    var add_button = $(".add");

    var x = 1;
    $(add_button).click(function(e) {
        e.preventDefault();
        if(x < max_fields) {
            x++;
            $(wrapper).append('<div><label>Enter your hobby: <input type="text" name="hobby[]"></label><br><br></div>');
        }
    });
});
