console.log("Scripted loaded");

document.addEventListener("DOMContentLoaded",() => {
    // initial
changeTheme();

});

// change theme work
let currentTheme=getTheme();

//set to web page
function changeTheme(){
    changePageTheme(currentTheme,currentTheme);
    // set the listener to change theme button
    const changeThemeButton = document.querySelector('#theme_change_button');

    changeThemeButton.addEventListener("click" ,(event) => {
        let oldTheme =currentTheme;
        console.log("button clicked");
        

        if (currentTheme === "dark"){
             currentTheme = "light";
        }
        else{
            currentTheme = "dark";
        }
       changePageTheme(currentTheme,oldTheme);
          
    });
}

// set theme to localStorage
function setTheme (theme){
    localStorage.setItem("theme",theme);
}

// get theme from localStorage
function getTheme (){
    let theme = localStorage.getItem("theme");
   return theme ? theme : "light";
}

//change current page theme
function  changePageTheme(theme,oldTheme){
    //update into localStorage 
    setTheme(currentTheme);

    document.querySelector("html").classList.remove(oldTheme);

    document.querySelector("html").classList.add(theme);
    
    document.querySelector('#theme_change_button').querySelector("span").textContent = theme == "light" ? "Dark" : "Light";


}