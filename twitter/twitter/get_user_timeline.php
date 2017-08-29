<?php

session_start();
require_once('twitteroauth/twitteroauth.php');
require_once('config.php');
header('Content-Type: application/json');

$connection = new TwitterOAuth(CONSUMER_KEY, CONSUMER_SECRET, $_GET["oauth_token"], $_GET["oauth_token_secret"]);
$statuses = $connection->get("statuses/user_timeline", ["screen_name" => $_GET["screen_name"]]);
echo json_encode($statuses);

?>
