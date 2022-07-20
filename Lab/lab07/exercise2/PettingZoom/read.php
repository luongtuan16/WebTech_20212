<?php 
    $file = "pet.xml";
    $xml = simplexml_load_file($file) or die ("Unable to load XMLfile!");
    echo "Name: " . $xml->name . "\n";
    echo "Age: " . $xml->age . "\n"; 
    echo "Species: " . $xml->species . "\n"; 
    echo "Parents: " . $xml->parents->mother." and " .$xml->parents->father . "\n"; 
?> 