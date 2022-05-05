<!DOCTYPE html>
<html>
	<head>
		<title>Business Listing</title>
		<meta charset="UTF-8">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
	</head>
	<body>
		<?php
		echo '<form method="post">';
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
			echo '<input type="submit" name="' . $item[0] . '" value="' . $item[1] . '"><br><br>';
		}
		echo '</form>';

		foreach ($catArr as $item) {
			if (isset($_POST[$item[0]])) {
				try {
					$conn = new PDO("mysql:host=localhost; dbname=Bizness", 'root', '');
					$conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

					$sql = 'select Name, Address, City, Telephone, URL from (Businesses inner join Biz_Categories on Businesses.BusinessID = Biz_Categories.BusinessID) inner join Categories on Biz_Categories.CategoryID = Categories.CategoryID where Title like \'' . $item[1] . '\'';
					$result = $conn->prepare($sql);
					$result->execute();
					$result->setFetchMode(PDO::FETCH_ASSOC);
					$result = $result->fetchAll();

					echo '<table>';
					foreach ($result as $item) {
						echo '<tr>';
						echo '<td>' . $item['Name'] . '</td>';
						echo '<td>' . $item['Address'] . '</td>';
						echo '<td>' . $item['City'] . '</td>';
						echo '<td>' . $item['Telephone'] . '</td>';
						echo '<td>' . $item['URL'] . '</td>';
						echo '</tr>';
					}
					echo '</table>';
				} catch (PDOException $e) {
					echo $e->getMessage();
				}
				$conn = null;
			}
		}
		?>
	</body>
</html>
