package com.backend.server.util;

public class CompareUtils {

    /**
     * [s1, e1] 범위와 [s2, e2] 범위가 겹치는지 검사합니다.
     * @throws IllegalArgumentException s1 > e1 이거나 s2 > e2 면 예외 발생
     * @throws NullPointerException 인자가 null 이면 예외 발생
     */
    public static <T extends Comparable<T>> boolean hasIntersectionInclusive(T s1, T e1, T s2, T e2) {
        if (s1 == null || e1 == null || s2 == null || e2 == null)
            throw new NullPointerException("null 을 비교하려 합니다.");

        if (s1.compareTo(e1) > 0 || s2.compareTo(e2) > 0)
            throw new IllegalArgumentException("비교 범위가 잘못되었습니다.");

        return s1.compareTo(e2) <= 0 && s2.compareTo(e1) <= 0;
    }

    /**
     * (s1, e1) 범위와 (s2, e2) 범위가 겹치는지 검사합니다.
     * @throws IllegalArgumentException s1 >= e1 이거나 s2 >= e2 면 예외 발생
     * @throws NullPointerException 인자가 null 이면 예외 발생
     */
    public static <T extends Comparable<T>> boolean hasIntersectionExclusive(T s1, T e1, T s2, T e2) {
        if (s1 == null || e1 == null || s2 == null || e2 == null)
            throw new NullPointerException("null 을 비교하려 합니다.");

        if (s1.compareTo(e1) >= 0 || s2.compareTo(e2) >= 0)
            throw new IllegalArgumentException("비교 범위가 잘못되었습니다. 시작 값은 끝 값보다 작아야 합니다.");

        return s1.compareTo(e2) < 0 && s2.compareTo(e1) < 0;
    }

    /**
     * (s,e) 범위에 o 가 포함되는지 확인합니다.
     */
    public static <T extends Comparable<T>> boolean isBetweenExclusive(T s, T e, T o) {
        return isBetween(s, e, o, false, false);
    }

    /**
     * [s,e] 범위에 o 가 포함되는지 확인합니다.
     */
    public static <T extends Comparable<T>> boolean isBetweenInclusive(T s, T e, T o) {
        return isBetween(s, e, o, true, true);
    }

    /**
     * 주어진 범위에 o 가 포함되는지 확인합니다.
     * @throws IllegalArgumentException s > e 면 예외 발생
     * @throws NullPointerException 인자가 null 이면 예외 발생
     */
    public static <T extends Comparable<T>> boolean isBetween(T s, T e, T o, boolean inclusiveStart, boolean inclusiveEnd) {
        if (s == null || e == null || o == null)
            throw new NullPointerException("null 을 비교하려 합니다.");

        if (s.compareTo(e) > 0)
            throw new IllegalArgumentException("비교 범위가 잘못되었습니다.");

        boolean afterStart = inclusiveStart ? o.compareTo(s) >= 0 : o.compareTo(s) > 0;
        boolean beforeEnd  = inclusiveEnd   ? o.compareTo(e) <= 0 : o.compareTo(e) < 0;

        return afterStart && beforeEnd;
    }
}