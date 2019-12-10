const CLIENT_ID = "215502059190-20b8ju09m2hnk9k4facg32fo5am1gcq7.apps.googleusercontent.com";

(() =>  {
    gapi.load('auth2', function () {
        auth2 = gapi.auth2.init({
            client_id: CLIENT_ID,
            scope: "https://www.googleapis.com/auth/calendar.events https://www.googleapis.com/auth/calendar.readonly"
        });
    });
})();