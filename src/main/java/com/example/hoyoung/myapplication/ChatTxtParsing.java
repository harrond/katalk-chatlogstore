package com.example.hoyoung.myapplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Hoyoung on 2017-01-31.
 */

public class ChatTxtParsing {
    private String roomName;
    private String createDate;
    private String path;
    private String fileName;
    private File file;
    private FileReader fileReader;
    private BufferedReader br;
    public ChatTxtParsing(String path,String name){
        this.path=path;
        this.fileName=name;
        try{

            file=new File(path+fileName);
            fileReader=new FileReader(file);
            br=new BufferedReader(fileReader);
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }


    }
    public String getRoomName(){
        return roomName;
    }
    public String getCreateDate(){
        return createDate;
    }
    public ArrayList parsing(){
        int flag = 0;//0이면 PC 1이면 Phone에서 뽑은 것
        ArrayList<MsgItem> list =new ArrayList<MsgItem>();
        String name="",time="";
        int type=2;
        if(path==null||fileName==null) return null;
        try {
            String tmp = br.readLine();
            roomName=tmp;
            String tmp2 = br.readLine();
            createDate=createFrom(tmp2);
            if(createDate==null) {
                flag = 1;
                createDate=phoneCreateDateMatch(tmp2);
            }


            while (true) {
                String line = br.readLine();

                if(line == null) {
                    break;
                }
                if(flag==0){
                    if(line.equals("")){

                    }
                    else if(line.charAt(0)=='['){

                        MsgItem item = pcMatch(line);
                        list.add(item);
                        type=item.getType();
                        time=item.getTime();
                        name=item.getName();
                    }
                    else if(line.charAt(0)=='-'){

                        String s = line.replaceAll("-","");
                        list.add(new MsgItem(s,2,"0","날짜"));

                    }
                    else{
                        if(!line.equals("")){
                            if(isInvite(line)) list.add(new MsgItem(line,2,"0","입출"));
                            else list.add(new MsgItem(line,type,time,name));
                        }
                    }
                }
                else {

                    MsgItem item = phoneMatch(line);
                    if(item!=null){
                        list.add(item);
                        type=item.getType();
                        time=item.getTime();
                        name=item.getName();
                    }
                    else{

                        String str=phoneDateMatch(line);
                        if(str == null) {
                            if(isDateStr(line)){
                                list.add(new MsgItem(line,2,"0","날짜"));
                            }
                            else {
                                if(!line.equals("")) list.add(new MsgItem(line, type, time, name));
                            }
                        }
                        else list.add(new MsgItem(str,2,"0","입출"));

                    }
                }

            }
        }catch(Exception ex){

            System.out.println(ex.getMessage());
        }


        return list;

    }

    private static String createFrom(String input){
        String date=null;
        Pattern pa = Pattern.compile("^.*\\s\\:\\s([0-9]*)\\-([0-9]*)\\-([0-9]*)\\s[0-9]*\\:[0-9]*\\:[0-9]*$");
        Matcher mat = pa.matcher(input);
        if(mat.matches()) {
            date = mat.group(1)+". "+mat.group(2)+". "+mat.group(3)+".";
            return date;
        }
        else return date;
    }
    private static String phoneCreateDateMatch(String input){
        String date=null;
        Pattern pa = Pattern.compile("^.*\\s\\:\\s([0-9]*)년\\s([0-9]*)월\\s([0-9]*)일\\s.*\\s[0-9]*\\:[0-9]*$");
        Matcher mat = pa.matcher(input);
        if(mat.matches()) {
            date = mat.group(1)+". "+mat.group(2)+". "+mat.group(3)+".";
            return date;
        }
        else return date;
    }
    private static MsgItem phoneMatch(String input){
        Pattern pa = Pattern.compile("^([0-9]*년\\s[0-9]*월\\s[0-9]*일)\\s(.*\\s[0-9]*\\:[0-9]*)\\,\\s(.*)\\s\\:\\s(.*)$");
        Matcher mat = pa.matcher(input);
        MsgItem item=null;
        if(mat.matches()){
            if(mat.group(3).equals("회원님")){
                item=new MsgItem(mat.group(4),1,mat.group(2),mat.group(3));
            }
            else{
                item=new MsgItem(mat.group(4),0,mat.group(2),mat.group(3));
            }
        }

        return item;

    }
    private static MsgItem pcMatch(String input){
        Pattern pa = Pattern.compile("^\\[(.*)\\]\\s\\[(.*)\\]\\s(.*)$");
        Matcher mat = pa.matcher(input);
        MsgItem item=null;

        if(mat.matches()) {
            if(mat.group(1).equals("신혜")) {
                item = new MsgItem(mat.group(3), 1, mat.group(2),mat.group(1) );
            }
            else item = new MsgItem(mat.group(3), 0, mat.group(2),mat.group(1) );

        }
        return item;
    }
    private static String phoneDateMatch(String input){
        String str=null;
        Pattern pa = Pattern.compile("^([0-9]*년\\s[0-9]*월\\s[0-9]*일)\\s(.*\\s[0-9]*\\:[0-9]*)\\,\\s(.*)$");
        Matcher mat = pa.matcher(input);
        if(mat.matches()){
            str=mat.group(3);
        }
        return str;
    }
    private static boolean isDateStr(String input){
        Pattern pa = Pattern.compile("^([0-9]*년\\s[0-9]*월\\s[0-9]*일)\\s(.*\\s[0-9]*\\:[0-9]*)$");
        Matcher mat = pa.matcher(input);
        if(mat.matches()){
            return true;
        }
        return false;
    }
    private static boolean isInvite(String input){
        Pattern pa = Pattern.compile("^.*님이\\\\s.*님을\\\\s초대하였습니다.$");
        Matcher mat = pa.matcher(input);
        if(mat.matches()){
            return true;
        }
        return false;

    }
}
