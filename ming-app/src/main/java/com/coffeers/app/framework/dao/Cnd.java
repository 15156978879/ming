package com.coffeers.app.framework.dao;

import com.coffeers.app.framework.utils.StringUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/21 0021.
 */
public class Cnd {

    private static List<ArrayList<Object>> lists =new ArrayList<>();
    private static Cnd cnd = new Cnd();

    private Cnd() {}

    public static Cnd NEW() {
        cnd = new Cnd();
        return cnd;
    }


    public static void where(String operand1,String operator,Object operand2){
        ArrayList<Object> arrayList = Cnd.getList("where",operand1,operator,operand2);
        lists.add(arrayList);
    }

    public static Cnd and(String operand1,String operator,Object operand2){
        ArrayList<Object> arrayList = Cnd.getList("and",operand1,operator,operand2);
        lists.add(arrayList);
        return cnd;
    }

    public static Cnd andOrs(Cnd and,Cnd... or){
        return cnd;
    }

    public static Cnd orAnds(Cnd or,Cnd... and){
        return cnd;
    }

    public static Cnd or(String operand1,String operator,Object operand2){
        ArrayList<Object> arrayList = Cnd.getList("or",operand1,operator,operand2);
        lists.add(arrayList);
        return cnd;
    }

    public static void sql(String sql){

    }

    public static void count(){}


    private static ArrayList<Object> getList(String oper,String operand1,String operator,Object operand2){
        ArrayList<Object> arrayList = new ArrayList<>();
        if(!StringUtil.isEmpty(operand1) && !StringUtil.isEmpty(operator) && operand2!=null){
            arrayList.add(oper);
            arrayList.add(operand1);
            arrayList.add(operator);
            arrayList.add(operand2);
        }
        return arrayList;
    }

    public static String getSql(){
        StringBuffer sb = new StringBuffer();
        //增强型for循环遍历
        for(ArrayList<Object> arrayLists : lists){
            for(int i=0;i<arrayLists.size();i++){
                if(i==arrayLists.size()-1){
                    if(arrayLists.get(i) instanceof String){
                        sb.append("'"+arrayLists.get(i).toString()+"' ");
                    }else if(arrayLists.get(i) instanceof Integer || arrayLists.get(i) instanceof Float || arrayLists.get(i) instanceof Double){
                        sb.append(arrayLists.get(i).toString()+" ");
                    }else if(arrayLists.get(i) instanceof Boolean){
                        if((Boolean)arrayLists.get(i)){
                            sb.append(1+" ");
                        }else{
                            sb.append(0+" ");
                        }
                    }else{
                        sb.append(arrayLists.get(i).toString()+" ");
                    }
                }else {
                    sb.append(arrayLists.get(i).toString()+" ");
                }
            }
        }
        String str = "";
        if(!sb.toString().contains("where")){
            //str+=" where 1=1 ";
            str+=sb.toString();
        }else{
            str=sb.toString().replace("where","");
        }
        System.out.println(str);
        return str;
    }


    public static void main(String[] args){
        Cnd cnd = Cnd.NEW();
        //cnd.andOrs(Cnd.NEW().and("age","=",false),Cnd.NEW().or("sex","=","男").or("sex","=","nv"));

        cnd.where("name","=","zhangsan");
        cnd.and("age","=",22).and("age","=",25);
        cnd.or("sex","=","男").or("sex","=","nv");
        getSql();
    }

}
