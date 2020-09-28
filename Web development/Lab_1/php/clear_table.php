<?php
session_start();

if (isset($_SESSION["redgry"])){
    $_SESSION["redgry"] = array();
}

include "add_in_table.php";