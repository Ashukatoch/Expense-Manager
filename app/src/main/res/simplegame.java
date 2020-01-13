package Codechef;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.*;

public class simplegame
{
    public static class node
    {
        long val;
        long lv;
        node(long val)
        {
            this.val=val;
        }
        node(long val,long lv)
        {
            this.val=val;
            this.lv=lv;
        }


    }
    public static class comparator implements Comparator<node>
    {

        @Override
        public int compare(node o1, node o2)
        {
            if(o1.lv>o2.lv)
                return -1;
            else if(o2.lv==o1.lv)
            {
                return 0;
            }
            else
                return 1;
        }
    }
    public static void main(String args[]) throws IOException {
        int t,n;

        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
        t=Integer.parseInt(br.readLine());
        while(t-->0)
        {
            ArrayList<Long> even=new ArrayList<>();
            ArrayList<Long> Lv=new ArrayList<>();
            n=Integer.parseInt(br.readLine());
            long ans=0;
            for(int i=0;i<n;i++)
            {
                String input[]=br.readLine().split(" ");
                int cl=Integer.parseInt(input[0]);
                int ei;
                long e=0,oc=0;
                if(cl%2==0)
                {
                    ei=cl/2;
                    for(int j=1;j<=ei;j++)
                    {
                        //System.out.println(input[j]);
                        e+=Long.parseLong(input[j]);
                    }
                    ans+=e;
                }
                else
                {
                    ei=cl/2+1;
                    for(int j=1;j<=ei;j++)
                    {
                        oc+=Long.parseLong(input[j]);
                    }
                    long lv;
                    lv=Long.parseLong(input[ei]);
                    ans+=oc-lv;
                    Lv.add(lv);
                }

            }
            int count=0;
                 Collections.sort(Lv,Collections.reverseOrder());
                for(int i=0;i<Lv.size();i++)
                {
                    long s =Lv.get(i);
                    if (count % 2 == 0)
                    {
                        ans +=s;
                    }
                    count++;
                }
            System.out.println(ans);
        }


        }
    }

