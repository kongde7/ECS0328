package com.aliyun.ecs;

public class Table
{
	String nameN1;
	String nameN2;
	String nameN3;
	
	String nameC1_l;
	String nameC1_xl;
	String nameC1_2xl;
	
	String nameG1_l;
	String nameG1_xl;
	String nameG1_2xl;
	
	String nameR1_l;
	String nameR1_xl;
	String nameR1_2xl;
	
	int cpuC1_l;
	int cpuC1_xl;
	int cpuC1_2xl;
	
	int cpuG1_l;
	int cpuG1_xl;
	int cpuG1_2xl;
	
	int cpuR1_l;
	int cpuR1_xl;
	int cpuR1_2xl;
	
	int memoryC1_l;
	int memoryC1_xl;
	int memoryC1_2xl;
	
	int memoryG1_l;
	int memoryG1_xl;
	int memoryG1_2xl;
	
	int memoryR1_l;
	int memoryR1_xl;
	int memoryR1_2xl;
	

	int numN1;
	int numN2;
	int numN3;
	
	double perC1_l;
	double perC1_xl;
	double perC1_2xl;
	double perG1_l;
	double perG1_xl;
	double perG1_2xl;
	double perR1_l;
	double perR1_xl;
	double perR1_2xl;
	
	public Table()
	{
		nameN1 = "NT-1-2";
		nameN2 = "NT-1-4";
		nameN3 = "NT-1-8";
		
		nameC1_l = "ecs.c1.large";
		nameC1_xl = "ecs.c1.xlarge";
		nameC1_2xl = "ecs.c1.2xlarge";
		
		nameG1_l = "ecs.g1.large";
		nameG1_xl = "ecs.g1.xlarge";
		nameG1_2xl = "ecs.g1.2xlarge";
		
		nameR1_l = "ecs.r1.large";
		nameR1_xl = "ecs.r1.xlarge";
		nameR1_2xl = "ecs.r1.2xlarge";
		
		cpuC1_l = 2;
		cpuC1_xl = 4;
		cpuC1_2xl = 8;
		
		cpuG1_l = 2;
		cpuG1_xl = 4;
		cpuG1_2xl = 8;
		
		cpuR1_l = 2;
		cpuR1_xl = 4;
		cpuR1_2xl = 8;
		
		memoryC1_l = 4;
		memoryC1_xl = 8;
		memoryC1_2xl = 16;
		
		memoryG1_l = 8;
		memoryG1_xl = 16;
		memoryG1_2xl = 32;
		
		memoryR1_l = 16;
		memoryR1_xl = 32;
		memoryR1_2xl = 64;
		
		perC1_l = 0.39;
		perC1_xl = 0.78;
		perC1_2xl = 1.56;
		perG1_l = 0.5;
		perG1_xl = 1;
		perG1_2xl = 2;
		perR1_l = 0.66;
		perR1_xl = 1.33;
		perR1_2xl = 2.65;
	}
	
	//输入需求的vm类型，可以放下就返回1，满了就返回0
	public void Use()
	{
		
	}
}
