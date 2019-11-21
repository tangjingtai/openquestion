package com.jt.utils;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public class CollectionExtension {

    public static <T> double avg(Collection<T> collection, Function<T, Double> keySelector){
        double sum = sum(collection,keySelector);
        return sum/collection.size();
    }

    public static <T> double sum(Collection<T> collection, Function<T, Double> keySelector){
        double sum = 0.0;
        for(T item : collection){
            double value = keySelector.apply(item);
            sum += value;
        }
        return sum;
    }

    public static <T> Optional<T> firstOrDefault(Collection<T> collection, Predicate<T> predicate){
        for(T item : collection){
            boolean b = predicate.test(item);
            if(b){
                return Optional.of(item);
            }
        }
        return Optional.empty();
    }

    public static <T,R extends Comparable<R>> R max(Collection<T> collection, Function<T, R> keySelector){
        return extremum(collection,keySelector,true);
    }

    public static <T,R extends Comparable<R>> R min(Collection<T> collection, Function<T, R> keySelector){
        return extremum(collection,keySelector,false);
    }

    private static <T,R extends Comparable<R>> R extremum(Collection<T> collection, Function<T, R> keySelector, boolean max){
        R result = null;
        boolean first = true;
        for(T item : collection){
            R temp = keySelector.apply(item);
            if(first){
                result = temp;
                first = false;
                continue;
            }
            if((max && result.compareTo(temp) < 0)
               || (!max && result.compareTo(temp) > 0)){
                result = temp;
            }
        }
        return result;
    }
}
