<!DOCTYPE html>
<html>
	<head>
		<title>Business Registration</title>
		<meta charset="UTF-8">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
	</head>
	<body>
		<?php
		echo '
		<form method="post">
			<table>
				<tr>
					<td>Business ID:</td>
					<td><input type="text" name="businessid"></td>
				</tr>
				<tr>
					<td>Business name:</td>
					<td><input type="text" name="businessname"></td>
				</tr>
				<tr>
					<td>Address:</td>
					<td><input type="text" name="address"></td>
				</tr>
				<tr>
					<td>City:</td>
					<td><input type="text" name="city"></td>
				</tr>
				<tr>
					<td>Telephone:</td>
					<td><input type="text" name="telephone"></td>
				</tr>
				<tr>
					<td>URL:</td>
					<td><input type="text" name="url"></td>
				</tr>
			</table>';

		echo 'Choose one, or multiple categories:<br>';
		$catArr = array();
		try {
			$conn = new PDO("mysql:host=localhost; dbname=business_service", 'root', '');
			$conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

			$result = $conn->prepare("select CategoryID, Title from Categories");
			$result->execute();
			$result->setFetchMode(PDO::FETCH_ASSOC);
			$result = $result->fetchAll();

			foreach ($result as $item) {
				array_push($catArr, array($item['CategoryID'], $item['Title']));
			}
		} catch (PDOException $e) {
			echo $e->getMessage();
		}
		$conn = null;

		foreach ($catArr as $item) {
			echo '<input type="checkbox" id="' . $item[0] . '" name="' . $item[0] . '">';
			echo '<label for="' . $item[0] . '">' . $item[1] . '</label><br>';
		}
		echo '<input type="submit" name="addbusiness" value="Add Business">';
		echo '</form>';

		if (isset($_POST['addbusiness'])) {
			try {
				$conn = new PDO("mysql:host=localhost; dbname=business_service", 'root', '');
				$conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

				$conn->exec('insert into Businesses (BusinessID, Name, Address, City, Telephone, URL) values (
					\'' . $_POST['businessid'] . '\',
					\'' . $_POST['businessname'] . '\',
					\'' . $_POST['address'] . '\',
					\'' . $_POST['city'] . '\',
					\'' . $_POST['telephone'] . '\',
					\'' . $_POST['url'] . '\'' . ')');

				foreach ($catArr as $item) {
					if (isset($_POST[$item[0]])) {
						$conn->exec('insert into Biz_Categories (BusinessID, CategoryID) values (
							\'' . $_POST['businessid'] . '\',
							\'' . $item[0] . '\'' . ')');
					}
				}
			} catch (PDOException $e) {
				echo $e->getMessage();
			}
			$conn = null;
			echo 'Done.<br>';
		}
		?>
	</body>
</html>
