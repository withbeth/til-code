package org.example.generic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Item33TypeSafeHeterogeneousContainer {

    /**
     * 하고싶은것:
     * 타입별로 즐겨찾는 인스턴스를 저장하고 검색하는 Favorites 클래스가 있으면 좋겠다.
     * 이때, 각 타입의 Class 객체를 Key 역할로 사용.
     * 이는, class의 Class가 제네릭이기때문에 가능하다.
     * 즉, class 리터럴의 타입은, Class가 아닌, Class<T>
     */
    interface Favorites {
        <T> void put(Class<T> type, T instance);
        <T> T get(Class<T> type);
    }

    // Impl
    static class TypeSafeHeterogeneousFavorites implements Favorites {

        // unbounded wild card 사용하여, 어떠한 클래스 타입토큰이라도 저장 가능(실체화 불가능한 애들 제외)
        private final Map<Class<?>, Object> map = new HashMap<>();

        @Override
        public <T> void put(Class<T> type, T instance) {
            // null 방지
            Objects.requireNonNull(type);
            // type.cast()를 통해, type으로 형변환이 가능한 애들만 저장하도록 보증.
            map.put(type, type.cast(instance));
        }

        @Override
        public <T> T get(Class<T> type) {
            // put시, 형변환 가능한 애들만 저장하도록 보증하였으므로, 여기서 casting이 실패할 일은 없다.
            // 즉, 비검사 형변환 할 필요 없다.
            return type.cast(map.get(type));
        }
    }


    // Client
    public static void main(String[] args) {
        Favorites f = new TypeSafeHeterogeneousFavorites();

        // OK Case
        f.put(String.class, "Java");
        f.put(Integer.class, 3);
        f.put(Class.class, Favorites.class);

        String favoriteStr = f.get(String.class);
        Integer favoriteInt = f.get(Integer.class);
        Class favoriteClass = f.get(Class.class);

        System.out.println("favoriteStr = " + favoriteStr);
        System.out.println("favoriteInt = " + favoriteInt);
        System.out.println("favoriteClass = " + favoriteClass);
    }


    /**
     * from Java tutorial
     * https://docs.oracle.com/javase/tutorial/extra/generics/literals.html
     *
     * 하고싶은것:
     * - create a utility method that performs a database query,
     * - given as a string of SQL, and returns a collection of objects in the database that match that query.
     */
    private static class SQLUtil {

        private static class EmpInfo {
        }

        /**
         * Raw type 이용하는 버전.
         * 예: Collection emps = SQLUtil.selectWithOutGeneric(EmpInfo.class, "select * from emps");
         */
        public Collection selectWithOutGeneric(Class clazz, String sqlStatement) {
            Collection result = new ArrayList();

            // run sql query using jdbc

            // use reflection and set all of item's fields from sql result.
            try {

                Object item = clazz.getDeclaredConstructor().newInstance();

            } catch (ReflectiveOperationException e) {

                throw new RuntimeException(e);

            }
            return result;
        }

        /**
         * Class<T> Generic 이용하는 버전.
         * 예: Collection<EmpInfo> emps = SQLUtil.selectWithGeneric(EmpInfo.class, "select * from emps");
         */
        public <T> Collection<T> selectWithGeneric(Class<T> clazz, String sqlStatement) {
            Collection<T> result = new ArrayList<>();

            // run sql query using jdbc

            // use reflection and set all of item's fields from sql result.
            try {

                Object item = clazz.getDeclaredConstructor().newInstance();

            } catch (ReflectiveOperationException e) {

                throw new RuntimeException(e);

            }
            return result;
        }


    }

}
