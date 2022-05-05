<!DOCTYPE html>
<html>
	<head>
		<title>Category Administration</title>
		<meta charset="UTF-8">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
	</head>
	<body>
		<?php
		try {
			$conn = new PDO("mysql:host=localhost; dbname=business_service", 'root', '');
			$conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

			$result = $conn->prepare("select * from Categories");
			$result->execute();
			$result->setFetchMode(PDO::FETCH_ASSOC);
			$result = $result->fetchAll();

			echo '<table border="1px solid black;">';
			echo '
				<tr>
					<th align="center">Cat ID</th>
					<th align="center">Title</th>
					<th align="center">Description</th>
				</tr>
			';
			foreach ($result as $item) {
				echo '<tr>';
				echo '<td>' . $item['CategoryID'] . '</td>';
				echo '<td>' . $item['Title'] . '</td>';
				echo '<td>' . $item['Description'] . '</td>';
        		echo '</tr>';
    		}
    		echo '</table>';
		} catch (PDOException $e) {
			echo $e->getMessage();
		}
		$conn = null;
		?>
		<br>
		<form method="post">
			<table>
				<tr>
					<td><input type="text" name="catid" placeholder="CategoryID"></td>
					<td><input type="text" name="title" placeholder="Title"></td>
					<td><input type="text" name="description" placeholder="Description"></td>
				</tr>
				<tr><td><input type="submit" name="addcategory" value="Add Category"></td></tr>
			</table>
		</form>
		<?php
		if (isset($_POST['addcategory'])) {
			try {
				$conn = new PDO("mysql:host=localhost; dbname=business_service", 'root', '');
				$conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

				$conn->exec('insert into Categories (CategoryID, Title, Description) values (\'' . $_POST['catid'] . '\', \'' . $_POST['title'] . '\', \'' . $_POST['description'] . '\'' . ')');
			} catch (PDOException $e) {
				echo $e->getMessage();
			}
			$conn = null;
			echo 'Done.<br>';
		}
		?>
	</body>
</html>
