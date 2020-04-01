package com.aliyun.ecs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class Elastic
{
	public static void main( String[] args ) throws IOException
	{
		int i = 1;
		int k=1;
		int flag=0;
		int temp=1;
		String fileName = "input_vm_" + String.valueOf( k ) + ".csv";
		VM vm = new VM();
		NC nc = new NC();
		Times times = new Times();
		Table table = new Table();
		Price price = new Price();
		Resource res = new Resource();
		Release test = new Release();
		Release rel = new Release();
		String date;
		
		//���ű����
		Adjust adjust = new Adjust();
		adjust.Index();
		
		//��ȡ��һ��vm����
		vm.ReadOne( i, fileName, times, test );
		
		//��ʼ����ǰʱ��
		times.setDate(vm.createDate);
		
		//��ʼ��NC̨����д��ncList��
		ArrayList<NC> ncList = new ArrayList<>();
		ArrayList<NC> ncListNew = new ArrayList<>();
		
		nc.Add( 20, 100, 5, ncList, times, res );
		price.OneCost( 10, 30, 5 );
		
		//����vmList������˳��д��
		ArrayList<VM> vmList = new ArrayList<>();
		ArrayList<VM> vmListA = new ArrayList<>();
		ArrayList<VM> vmListB = new ArrayList<>();
		ArrayList<VM> vmListC = new ArrayList<>();
		
		System.out.println( "��ʼ����Ŀ¼�µ�csv�ļ���������̴����Ҫ5����..." );
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
					nc.Enable( ncList, ncListNew, times, res );
				}
				
				times.NextSec();
				
				//����һ��vm����
				vm = new VM();
				if( vm.ReadOne( i, fileName, times, rel ) == 0 )
				{
					temp = i;
					flag=1;
					break;
				}
				else flag=0;
				
				//���֮ǰ�ȷ���
				vm.Assign( ncList, res, price );
				
				//��Դ��ҲҪͬ��д��
				res.Assign( vm, table );
				
				vmList.add( vm );
			}
			
			if( i<10001 )
			{
				if( flag==1 )
				{
					//���������д����NC��ˮ��vm��ˮ
					//vm.Write( vmList, times );
					nc.Write( "nc1.csv", ncList, times );
					
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
					
					//����23��59�֣���������ͳɱ�
					price.Income( table, res );
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
					
					
					//��������,�������˾Ͳ�������
					//adjust.Optimize( vmListA, ncList, res, times );

					//��������
					nc.Report( 2, 5, 1, ncListNew, times, price );
					nc.Write( "new_nc1.csv", ncListNew, times );
					
					
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
					fileName = "input_vm_" + String.valueOf( k ) + ".csv";
					temp = 1;
				}
				else
				{
					//���¿�Ϊ�ظ�
					//���������д����NC��ˮ��vm��ˮ
					//vm.Write( vmList, times );
					nc.Write( "nc1.csv", ncList, times );
					
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
					
					//����23��59�֣���������ͳɱ�
					price.Income( table, res );
					price.Cost( table, res );
					//System.out.println( "main�ɱ����ܣ�" +  price.sumCost );
					
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
					//nc.Write( ncList, times );//���Ŵ�ӡһ��
					

					//��������
					//nc.Report( 1, 2, 3, ncListNew, times, price );
					//nc.Write( "new_nc1.csv", ncListNew, times );
					
					
					times.NextDay();
					flag = 0;
					price.oneCost=0;
					price.dayCost=0;
					price.dayCost=0;
					price.buhuo =0;
					price.duangong=0;
					//����������ظ�
					
					break;
				}
			}
		}
		
		System.out.println( "�����������ʣ�" + (( price.sumIncome - price.sumCost ) / price.sumCost * 100) + "%" );//��ʱ��ɾ
	}
}
