//https://www.hackerrank.com/challenges/map-reduce-tutorials-2-the-group-by-operator
import java.util.*;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;  
import org.json.simple.JSONObject;  

class MapReduce
{
   private LinkedHashMap intermediate;
   private JSONObject finalResult = new JSONObject();
   private int resultCount;
   MapReduce()
   {
      resultCount=0;
      finalResult = new JSONObject();
      intermediate=new LinkedHashMap();
   }
   
   JSONObject execute(JSONObject inputdata)
   {
      for(int i=0;i<inputdata.size();i++)
      {
         JSONObject record=(JSONObject)inputdata.get(i);
         mapper(record);
      }
      
      Iterator it = intermediate.entrySet().iterator();
      while (it.hasNext()) {
         Map.Entry pair = (Map.Entry)it.next();
         reducer((String)pair.getKey(), (ArrayList)pair.getValue());
         it.remove();
      }
      return finalResult;
      
      
   }
   private void emit(LinkedHashMap obj)
   {
      finalResult.put(resultCount++,obj);
   }
   private <T> void reducer(T key, ArrayList value)
   {
      LinkedHashMap obj=new LinkedHashMap();
      obj.put("key",key);
      //Need to sort the data based on the requirement 
      Collections.sort(value);
      obj.put("value",value.toString().replace("[", "\"").replace("]", "\"").replace(", ", ","));
      emit(obj);
      
   }
   private void mapper(JSONObject record)
   {
      String state=(String)record.get("key");
      String city=(String)record.get("value");
      emitIntermediate(state,city);  //Add State-city pairs to the Map
      
   }
   private  <T1,T2>  void emitIntermediate(T1 key, T2 value)
   {
      if(!intermediate.containsKey(key))
         intermediate.put(key,new ArrayList());

      ArrayList temp=(ArrayList)intermediate.get(key);
      temp.add(value);
      intermediate.put(key,temp);
   }
}
public class Main
{
   
   public static void main(String []argh)
   {
      JSONObject inputdata= new JSONObject();
      Scanner sc=new Scanner(System.in);
      
      int linecount=0;
      while(sc.hasNext())
      {
         String line=sc.nextLine();
         String []temp=line.split("\t");
         
         Map obj=new JSONObject();
         obj.put("key",temp[0]);
         obj.put("value",temp[1]);
         
         inputdata.put(linecount++,obj);
      }
      MapReduce mapred= new MapReduce();
      JSONObject result=mapred.execute(inputdata); 
   
      for(int i=0;i<result.size();i++)
      {
         LinkedHashMap record=(LinkedHashMap)result.get(i);
         String key=(String)record.get("key");
         String value=(String)record.get("value");
         System.out.println ("{\"key\":\""+key+"\",\"value\":"+value+"}");

      }
   
   }
   
}

