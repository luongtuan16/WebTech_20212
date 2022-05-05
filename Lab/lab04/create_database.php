<?php
try {

	$conn = new PDO("mysql:host=localhost; dbname=Bizness", 'root', '');
	$conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	$conn->beginTransaction();
	/*
	$conn->exec("create database Bizness");
	$conn->exec("create table Businesses (
		BusinessID varchar(30) primary key,
		Name varchar(100) not null,
		Address varchar(200),
		City varchar(200),
		Telephone varchar(20),
		URL varchar(200)
		)");
	$conn->exec("create table Categories (
		CategoryID varchar(30) primary key,
		Title varchar(100) not null,
		Description varchar(200)
		)");
	$conn->exec("create table Biz_Categories (
		BusinessID varchar(30) not null,
		CategoryID varchar(30) not null,
		primary key (BusinessID, CategoryID),
		foreign key (BusinessID) references Businesses(BusinessID),
		foreign key (CategoryID) references Categories(CategoryID)
		)");
	*/
	$conn->commit();
	echo 'Done.<br>';
} catch (PDOException $e) {
	$conn->rollBack();
	echo $e->getMessage();
}
$conn = null;
