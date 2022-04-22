<html lang="en">
<head>
  <title>Receiving input...</title>
</head>
<body>
<div>
  Thank you: got your input.
</div>
<?php
$name = $_POST['name'];
$class = $_POST['class'];
$university = $_POST['university'];
$hobby = $_POST['hobby'];
print("Hello, " . $name . ".<br>");
print("You are studying at " . $class . ", " . $university . ".<br>");
print("Your hobbies is<br>");
for ($i = 0; $i < count($hobby); $i++) {
  print(($i + 1) . ". " . $hobby[$i] . "<br>");
}
?>
</body>
</html>
