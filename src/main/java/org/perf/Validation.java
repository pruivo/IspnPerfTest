package org.perf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.jgroups.util.Util;

/**
 * //TODO document this!
 *
 * @author Pedro Ruivo
 * @since 1.0
 */
public class Validation<K, V> {

   private final Map<K, List<V>> allData;
   private final Map<K, List<V>> errorsData;
   private final BiFunction<V, V, Boolean> equalsCheck;
   private final Function<List<V>, String> valuesToString;
   private int total_keys = 0;
   private int tot_errors = 0;

   public Validation(int numberOfKeys) {
      this(numberOfKeys, Objects::equals, String::valueOf);
   }

   public Validation(int numberOfKeys, BiFunction<V, V, Boolean> equalsCheck, Function<List<V>, String> toString) {
      this.errorsData = new HashMap<>();
      this.allData = new HashMap<>(numberOfKeys);
      this.equalsCheck = equalsCheck;
      this.valuesToString = toString;
   }

   public void addMember(String mbr, Map<K, V> data) {
      int size = data.size();
      int errors = 0;
      total_keys += size;

      System.out.printf("-- Validating contents of %s (%,d keys): ", mbr, size);
      for (Map.Entry<K, V> entry : data.entrySet()) {
         K key = entry.getKey();
         V val = entry.getValue();

         List<V> values = allData.get(key);
         if (values == null) { // key has not yet been added
            allData.put(key, values = new ArrayList<>());
            values.add(val);
         } else {
            if (values.size() >= 2) {
               System.err.printf("key %s already has 2 values\n", key);
            } else {
               values.add(val);
            }
            V val1 = values.get(0);
            for (V value : values) {
               if (!equalsCheck.apply(value, val1)) {
                  errors++;
                  tot_errors++;
                  errorsData.put(key, values);
                  break;
               }
            }
         }
      }
      if (errors > 0) {
         System.err.printf("FAIL: %d errors\n", errors);
      } else {
         System.out.print("OK\n");
      }
   }

   public void printResult() {
      System.out
            .println(Util.bold(String.format("\nValidated %,d keys total, %,d errors\n\n", total_keys, tot_errors)));
      if (tot_errors > 0) {
         for (Map.Entry<K, List<V>> entry : errorsData.entrySet()) {
            K key = entry.getKey();
            List<V> values = entry.getValue();
            System.err.printf("%s:\n%s\n", key, valuesToString.apply(values));
         }
      }
   }
}
