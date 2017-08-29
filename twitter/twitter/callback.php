<?php

session_start();
require_once('twitteroauth/twitteroauth.php');
require_once('config.php');

$content = null;
$connection = null;

if(isset($_REQUEST['oauth_verifier'])){

    $connection = new TwitterOAuth(CONSUMER_KEY, CONSUMER_SECRET, $_SESSION['oauth_token'], $_SESSION['oauth_token_secret']);
    try{
        $access_token = $connection->getAccessToken($_REQUEST['oauth_verifier']);
        echo json_encode($access_token);
    }
    catch(Exception $e){
        header("HTTP/1.0 400 Error");
        echo "\n\nFailed retrieving access token: " .$e->getMessage();
        exit;
    }

}

?>

