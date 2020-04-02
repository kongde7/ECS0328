package com.aliyun.ecs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Elastic
{
	public static void main( String[] args ) throws IOException
	{
		int max;
		int reNC=0;
		int newN1=0, newN2=0, newN3=0;
		int logN1, logN2, logN3;
		int a, b, c;
		int i = 1;
		int k=1;
		int flag=0;
		int temp=1;
		String fileName = "input_vm" + File.separator + "input_vm_" + String.valueOf( k ) + ".csv";
		VM vm = new VM();
		NC nc = new NC();
		Times times = new Times();
		Table table = new Table();
		Price price = new Price();
		Resource res = new Resource();
		String date;
		Log log = null;
		
		ArrayList<NC> ncList = new ArrayList<>();
		ArrayList<NC> ncListNew = new ArrayList<>();
		ArrayList<VM> vmList = new ArrayList<>();
		ArrayList<VM> vmListA = new ArrayList<>();
		ArrayList<VM> vmListB = new ArrayList<>();
		ArrayList<VM> vmListC = new ArrayList<>();
		ArrayList<VM> vmListStop = new ArrayList<>();
		ArrayList<Log> logList = new ArrayList<>();
		
		//���ű����
		Adjust adjust = new Adjust();
		adjust.Index();
		
		//��Ŀ¼�¾�����ļ�ȥ��
		Files files = new Files();
		files.First();
		
		//��ȡ��һ��VM����
		vm.ReadOne( i, fileName, times );
		
		//��ʼ����ǰʱ��
		times.setDate( vm.createDate );
		times.count = 1;
		
		//��ʼ��NC̨����д��ncList��
		nc.Add( 50, 100, 10, ncList, times, res );
		price.OneCost( 50, 100, 10 );
		
		//����vmList������˳��д��
		System.out.println( "��ʼ����input_vmĿ¼�µ�csv�ļ���������̴����Ҫ5����..." );
		while( true )
		{
			date = times.getDate();
			if( temp!=1 )
			{
				vmList.addAll( vmListA );
				vmListA.clear();
				vmListB.clear();
				System.out.println( "���ڼ���" + times.getDate() + "����..." );
			}
			for( i=temp; i<10001; i++ )
			{
				//�ж��ǲ��ǵ�0�룬�ǵĻ�ʹ�������
				if( times.getTime().contentEquals( "00:00:00" ) )
				{
					reNC = reNC - nc.Enable( ncList, ncListNew, times, res, price );
				}
				
				times.NextSec();
				
				//����һ��VM����
				vm = new VM();
				//flag=1��ʾһ����ʾ��������ˣ�Ҫ������һ����
				if( vm.ReadOne( i, fileName, times ) == 0 )
				{
					temp = i;
					flag=1;
					break;
				}
				else flag=0;
				
				//���֮ǰ�ȷ���
				if( vm.Assign( ncList, res, price ) == 0 )
				{
					//�������0��ʾ�������������д�Ϲ���
					vmListStop.add(vm);
				}
				else
				{
					//��������������䣬��Դ��ҲҪͬ���۳���Դ����
					res.Assign( vm, table );
				}
				//��vm������ӵ����У��Ժ����csv�ļ�
				vmList.add( vm );
			}
			
			if( i<10001 )
			{
				if( flag==1 )
				{
					//���������д����NC��ˮ��VM��ˮ
					//vm.Write( vmList, times );
					nc.Write( "nc.csv", ncList, times );
					
					for ( i = 0; i < vmList.size(); i++ )
					{
						vm = vmList.get(i);
						if( date.contentEquals( vm.createDate ) )
						{
							vmListC.add(vm);
						}
					}
					vm.Write( vmListC, times );
					vmListC.clear();
					
					//����23��59�֣���������
					price.Income( table, res );
					
					//��������,�������˾Ͳ�������
					//adjust.Optimize( vmListA, ncList, res, times );
					
					//ȫ�¼������̣������°�
					logN1 = 0;
					logN2 = 0;
					logN3 = 0;
					log = new Log();
					log.Count( vmList, vmListStop, times, res );
					logList.add( log );
					max = 0;
					for ( i = 0; i < logList.size(); i++ )
					{
						log = logList.get(i);
						if( log.cpuN1Today + log.cpuN2Today + log.cpuN3Today > max )
							max = log.cpuN1Today + log.cpuN2Today + log.cpuN3Today;
						logN1 = logN1 + log.cpuN1Today;
						logN2 = logN2 + log.cpuN2Today;
						logN3 = logN3 + log.cpuN3Today;
						if( times.getDate().contentEquals( log.date ) )
						{
							for ( i = 0; i < logList.size(); i++ )
							{
								log = logList.get(i);
								if( log.cpuN1Today + log.cpuN2Today + log.cpuN3Today == max )
								{
									logN1 = logN1 - log.cpuN1Today;
									logN2 = logN2 - log.cpuN2Today;
									logN3 = logN3 - log.cpuN3Today;
									break;
								}
							}
							//��CPU����������64ת���������������������N1����ܷ���64�ˣ�N2����ܷ���96�ˣ�N3����ܷ���64��
							logN1 = logN1 / ( i + 1 ) / 64;
							logN2 = logN2 / ( i + 1 ) / 96;
							logN3 = logN3 / ( i + 1 ) / 64;
							break;
						}
					}
					if( logN1 > res.numN1 + reNC )
					{
						newN1 = logN1 - res.numN1 - reNC;
					}
					if( logN2 > res.numN2 + reNC )
					{
						newN2 = logN2 - res.numN2 - reNC;
					}
					if( logN3 > res.numN3 + reNC )
					{
						newN3 = logN3 - res.numN3 - reNC;
					}
					reNC = newN1 + newN2 + newN3;
					System.out.println( "res.numN" + res.numN1+" "+reNC);
					System.out.println( "N1��N2��N3��������" + logN1 +" "+ logN2 +" "+ logN3 );//��ʱ��ɾ
					System.out.println( "N1��N2��N3������" + newN1 +" "+ newN2 +" "+ newN3 );//��ʱ��ɾ
					
					//����Ҫ��������̨NC
					res.usedCpuN1 = 0;
					res.usedCpuN2 = 0;
					res.usedCpuN3 = 0;
					res.usedMemoryN1 = 0;
					res.usedMemoryN2 = 0;
					res.usedMemoryN3 = 0;
					for ( i = 0; i < ncList.size(); i++ )
					{
						nc = ncList.get(i);
						if( nc.machineType.contentEquals( table.nameN1 ) )
						{
							res.usedCpuN1 = res.usedCpuN1 + nc.usedCpu;
							res.usedMemoryN1 = res.usedMemoryN1 + nc.usedMemory;
						}
						else if( nc.machineType.contentEquals( table.nameN2 ) )
						{
							res.usedCpuN2 = res.usedCpuN2 + nc.usedCpu;
							res.usedMemoryN2 = res.usedMemoryN2 + nc.usedMemory;
						}
						else if( nc.machineType.contentEquals( table.nameN3 ) )
						{
							res.usedCpuN3 = res.usedCpuN3 + nc.usedCpu;
							res.usedMemoryN3 = res.usedMemoryN3 + nc.usedMemory;
						}
					}
					/*
					System.out.println( "N1�����CPU������" + res.totalCpuN1 + "��" );//��ʱ��ɾ
					System.out.println( "N2�����CPU������" + res.totalCpuN2 + "��" );//��ʱ��ɾ
					System.out.println( "N3�����CPU������" + res.totalCpuN3 + "��" );//��ʱ��ɾ
					System.out.println( "N1�����ʹ�õ�CPU��" + res.usedCpuN1 + "��" );//��ʱ��ɾ
					System.out.println( "N2�����ʹ�õ�CPU��" + res.usedCpuN2 + "��" );//��ʱ��ɾ
					System.out.println( "N3�����ʹ�õ�CPU��" + res.usedCpuN3 + "��" );//��ʱ��ɾ
					*/
					
					//�������һ��������ʣ���������CPU�ಢ���ܴ�����õĶ࣬��Ϊ�ڴ�����
					
					//��������������������˲Ų���
					if( res.usedCpuN1 / times.count * 10 > res.numN1 * 64 - res.usedCpuN1 )
					//if( res.usedCpuN1 / times.count * 10 > res.totalCpuN1 - res.usedCpuN1 || 0.5 * res.totalCpuN1 < res.usedCpuN1 )
					{
						a = res.usedCpuN1 / times.count / 64;
					}
					else a = 0;
					if( res.usedCpuN2 / times.count * 10 > res.numN2 * 96 - res.usedCpuN2 )
					//if( res.usedCpuN2 / times.count * 10 > res.totalCpuN2 - res.usedCpuN2 || 0.5 * res.totalCpuN2 < res.usedCpuN2 )
					{
						b = res.usedCpuN2 / times.count / 96;
					}
					else b = 0;
					if( res.usedCpuN3 / times.count * 10 > res.numN3 * 64 - res.usedCpuN3 )
					//if( res.usedCpuN3 / times.count * 10 > res.totalCpuN3 - res.usedCpuN3 || 0.5 * res.totalCpuN3 < res.usedCpuN3 )
					{
						c = res.usedCpuN3 / times.count / 64;
					}
					else c = 0;
					
					System.out.println( "��������N1�������" + res.usedCpuN1/64 + "̨" );//��ʱ��ɾ
					System.out.println( "��������N2�������" + res.usedCpuN2/96 + "̨" );//��ʱ��ɾ
					System.out.println( "��������N3�������" + res.usedCpuN3/64 + "̨" );//��ʱ��ɾ
					System.out.println( "����ʣ��N1�������" + (res.numN1 * 64 - res.usedCpuN1)/64 + "̨" );//��ʱ��ɾ
					System.out.println( "����ʣ��N2�������" + (res.numN2 * 96 - res.usedCpuN2)/96 + "̨" );//��ʱ��ɾ
					System.out.println( "����ʣ��N3�������" + (res.numN3 * 64 - res.usedCpuN3)/64 + "̨" );//��ʱ��ɾ
					/*
					if( a!=0 )
						System.out.println( "����N1�������" + a + "̨" );//��ʱ��ɾ
					if( b!=0 )
						System.out.println( "����N2�������" + b + "̨" );//��ʱ��ɾ
					if( c!=0 )
						System.out.println( "����N3�������" + c + "̨" );//��ʱ��ɾ
					*/
					
					//��������
					
					nc.Report( 0, newN1+newN2+newN3, 0, ncListNew, times, price );
					newN1 = 0;
					newN2 = 0;
					newN3 = 0;
					nc.Write( "new_nc.csv", ncListNew, times );
					//�����ɱ�
					price.OneCost( 0, 0, 0 );
					price.Cost( table, res );
					
					//����23��59�֣��ͷŵ���Ҫ�ͷŵ���Դ��A�Ǵ�����B���ͷ�
					for ( i = 0; i < vmList.size(); i++ )
					{
						vm = vmList.get(i);
						if( date.contentEquals( vm.releaseDate ) )
						{
							vm.status = "release";
							vmListB.add(vm);
						}
						else
						{
							vmListA.add(vm);
						}
					}
					vmList.clear();
					res.release( vmListB, ncList, table );
					vm.Write( vmListB, times );
					
					times.NextDay();
					flag = 0;
					price.oneCost=0;
					price.dayCost=0;
					price.dayCost=0;
					price.buhuo = 0;
					price.duangong =0;
				}
			}
			

			
			else if( i>=10001 )
			{
				if( k<20 )
				{
					k++;
					fileName = "input_vm" + File.separator + "input_vm_" + String.valueOf( k ) + ".csv";
					temp = 1;
				}
				else
				{
					//���¿�Ϊ�ظ�����Ϊ�����һ�죬������һ��ѭ��
					//���������д����NC��ˮ��VM��ˮ
					//vm.Write( vmList, times );
					nc.Write( "nc.csv", ncList, times );
					
					for ( i = 0; i < vmList.size(); i++ )
					{
						vm = vmList.get(i);
						if( date.contentEquals( vm.createDate ) )
						{
							vmListC.add(vm);
						}
					}
					vm.Write( vmListC, times );
					vmListC.clear();
					
					//����23��59�֣���������
					price.Income( table, res );
					
					//��������,�������˾Ͳ�������
					//adjust.Optimize( vmListA, ncList, res, times );
					
					//ȫ�¼������̣������°�
					logN1 = 0;
					logN2 = 0;
					logN3 = 0;
					log = new Log();
					log.Count( vmList, vmListStop, times, res );
					logList.add( log );
					for ( i = 0; i < logList.size(); i++ )
					{
						log = logList.get(i);
						logN1 = logN1 + log.cpuN1Today;
						logN2 = logN2 + log.cpuN2Today;
						logN3 = logN3 + log.cpuN3Today;
						//System.out.println( "������������" + log.cpuN1Today +" "+ log.cpuN2Today +" "+ log.cpuN3Today + times.getDate()+log.date +i );//��ʱ��ɾ
						if( times.getDate().contentEquals( log.date ) )
						{
							//��CPU����������64ת���������������������N1����ܷ���64�ˣ�N2����ܷ���96�ˣ�N3����ܷ���64��
							logN1 = logN1 / ( i + 1 ) / 64;
							logN2 = logN2 / ( i + 1 ) / 96;
							logN3 = logN3 / ( i + 1 ) / 64;
							break;
						}
					}
					if( logN1 > res.numN1 + reNC )
					{
						newN1 = logN1 - res.numN1 - reNC;
					}
					if( logN2 > res.numN2 + reNC )
					{
						newN2 = logN2 - res.numN2 - reNC;
					}
					if( logN3 > res.numN3 + reNC )
					{
						newN3 = logN3 - res.numN3 - reNC;
					}
					System.out.println( "N1��N2��N3��������" + logN1 +" "+ logN2 +" "+ logN3 +" "+ logList.size() );//��ʱ��ɾ
					System.out.println( "N1��N2��N3������" + newN1 +" "+ newN2 +" "+ newN3 );//��ʱ��ɾ
					
					//����Ҫ��������̨NC
					res.usedCpuN1 = 0;
					res.usedCpuN2 = 0;
					res.usedCpuN3 = 0;
					res.usedMemoryN1 = 0;
					res.usedMemoryN2 = 0;
					res.usedMemoryN3 = 0;
					for ( i = 0; i < ncList.size(); i++ )
					{
						nc = ncList.get(i);
						if( nc.machineType.contentEquals( table.nameN1 ) )
						{
							res.usedCpuN1 = res.usedCpuN1 + nc.usedCpu;
							res.usedMemoryN1 = res.usedMemoryN1 + nc.usedMemory;
						}
						else if( nc.machineType.contentEquals( table.nameN2 ) )
						{
							res.usedCpuN2 = res.usedCpuN2 + nc.usedCpu;
							res.usedMemoryN2 = res.usedMemoryN2 + nc.usedMemory;
						}
						else if( nc.machineType.contentEquals( table.nameN3 ) )
						{
							res.usedCpuN3 = res.usedCpuN3 + nc.usedCpu;
							res.usedMemoryN3 = res.usedMemoryN3 + nc.usedMemory;
						}
					}
					
					/*
					System.out.println( "N1�����CPU������" + res.totalCpuN1 + "��" );//��ʱ��ɾ
					System.out.println( "N2�����CPU������" + res.totalCpuN2 + "��" );//��ʱ��ɾ
					System.out.println( "N3�����CPU������" + res.totalCpuN3 + "��" );//��ʱ��ɾ
					System.out.println( "N1�����ʹ�õ�CPU��" + res.usedCpuN1 + "��" );//��ʱ��ɾ
					System.out.println( "N2�����ʹ�õ�CPU��" + res.usedCpuN2 + "��" );//��ʱ��ɾ
					System.out.println( "N3�����ʹ�õ�CPU��" + res.usedCpuN3 + "��" );//��ʱ��ɾ
					*/
					
					//�������һ��������ʣ���������CPU�ಢ���ܴ�����õĶ࣬��Ϊ�ڴ�����
					
					//��������������������˲Ų���
					if( res.usedCpuN1 / times.count * 10 > res.numN1 * 64 - res.usedCpuN1 )
					//if( res.usedCpuN1 / times.count * 10 > res.totalCpuN1 - res.usedCpuN1 || 0.5 * res.totalCpuN1 < res.usedCpuN1 )
					{
						a = res.usedCpuN1 / times.count / 64;
					}
					else a = 0;
					if( res.usedCpuN2 / times.count * 10 > res.numN2 * 96 - res.usedCpuN2 )
					//if( res.usedCpuN2 / times.count * 10 > res.totalCpuN2 - res.usedCpuN2 || 0.5 * res.totalCpuN2 < res.usedCpuN2 )
					{
						b = res.usedCpuN2 / times.count / 96;
					}
					else b = 0;
					if( res.usedCpuN3 / times.count * 10 > res.numN3 * 64 - res.usedCpuN3 )
					//if( res.usedCpuN3 / times.count * 10 > res.totalCpuN3 - res.usedCpuN3 || 0.5 * res.totalCpuN3 < res.usedCpuN3 )
					{
						c = res.usedCpuN3 / times.count / 64;
					}
					else c = 0;
					/*
					System.out.println( "��������N1�������" + res.usedCpuN1/64 + "̨" );//��ʱ��ɾ
					System.out.println( "��������N2�������" + res.usedCpuN2/96 + "̨" );//��ʱ��ɾ
					System.out.println( "��������N3�������" + res.usedCpuN3/64 + "̨" );//��ʱ��ɾ
					System.out.println( "����ʣ��N1�������" + (res.numN1 * 64 - res.usedCpuN1)/64 + "̨" );//��ʱ��ɾ
					System.out.println( "����ʣ��N2�������" + (res.numN2 * 96 - res.usedCpuN2)/96 + "̨" );//��ʱ��ɾ
					System.out.println( "����ʣ��N3�������" + (res.numN3 * 64 - res.usedCpuN3)/64 + "̨" );//��ʱ��ɾ
					*/
					
					
					if( a!=0 )
						System.out.println( "����N1�������" + a + "̨" );//��ʱ��ɾ
					if( b!=0 )
						System.out.println( "����N2�������" + b + "̨" );//��ʱ��ɾ
					if( c!=0 )
						System.out.println( "����N3�������" + c + "̨" );//��ʱ��ɾ
					
					//��������
					reNC = reNC + newN1 + newN2 + newN3;
					nc.Report( newN1, newN2, newN3, ncListNew, times, price );
					newN1 = 0;
					newN2 = 0;
					newN3 = 0;
					nc.Write( "new_nc.csv", ncListNew, times );
					//�����ɱ�
					price.OneCost( 0, 0, 0 );
					price.Cost( table, res );
					
					//����23��59�֣��ͷŵ���Ҫ�ͷŵ���Դ��A�Ǵ�����B���ͷ�
					for ( i = 0; i < vmList.size(); i++ )
					{
						vm = vmList.get(i);
						if( date.contentEquals( vm.releaseDate ) )
						{
							vm.status = "release";
							vmListB.add(vm);
						}
						else
						{
							vmListA.add(vm);
						}
					}
					vmList.clear();
					res.release( vmListB, ncList, table );
					vm.Write( vmListB, times );
					
					System.out.println( "֮ǰ�ǵڼ��죺" + times.count );
					times.NextDay();
					flag = 0;
					price.oneCost=0;
					price.dayCost=0;
					price.dayCost=0;
					price.buhuo = 0;
					price.duangong =0;
					System.out.println( "�����棺" + price.sumIncome + "Ԫ" );//��ʱ��ɾ
					System.out.println( "�ܳɱ���" + price.sumCost + "Ԫ" );//��ʱ��ɾ
					//����������ظ�
					
					break;
				}
			}
		}
		
		System.out.println( "�����������ʣ�" + (( price.sumIncome - price.sumCost ) / price.sumCost * 100) + "%" );//��ʱ��ɾ
	}
}
