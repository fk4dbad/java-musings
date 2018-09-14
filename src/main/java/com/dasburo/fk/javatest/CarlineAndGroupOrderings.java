package com.dasburo.fk.javatest;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

/**
 * see ACO-1848
 * Ordering for carline groups and carlines.<br>
 * In the long run, this will require ordered lists per market, which might differ<br>
 * at least in the carline ordering, so it should be possible to obtain the lists with market parameters.<br>
 * also it might be necessary to configure these orderings per market on startup so a re-ordering in one market<br>
 * does not require a deployment of the app.<br>
 * it might be worth to consider a pure configuration approach in the very long run<br>
 * This class provides only the order, it does not sort anything!
 * @author asg
 *
 */
public final class CarlineAndGroupOrderings {

    private static final String GROUP_A = "a";
    private static final String GROUP_Q = "q";
    private static final String GROUP_TT = "tt";
    private static final String GROUP_R8 = "r8";

    private static final String CARLINE_A = "A";
    private static final String CARLINE_Q = "Q";
    private static final String CARLINE_TT = "TT";
    private static final String CARLINE_R8 = "R8";
    private static final String CARLINE_S = "S";
    private static final String CARLINE_TTS = "TTS";
    private static final String CARLINE_RS = "RS";
    private static final String CARLINE_TT_RS = "TT RS";

    private CarlineAndGroupOrderings() {

    }

    public static ImmutableList<String> getCarlineGroupOrder() {
        List<String> groupOrder = new ArrayList<>();
        groupOrder.add(GROUP_A);
        groupOrder.add(GROUP_Q);
        groupOrder.add(GROUP_TT);
        groupOrder.add(GROUP_R8);
        return ImmutableList.copyOf(groupOrder);
    }

    public static ImmutableList<String> getCarlineOrder() {
        List<String> carlineOrder = new ArrayList<>();
        carlineOrder.add(CARLINE_A);
        carlineOrder.add(CARLINE_Q);
        carlineOrder.add(CARLINE_TT);
        carlineOrder.add(CARLINE_R8);
        carlineOrder.add(CARLINE_S);
        carlineOrder.add(CARLINE_TTS);
        carlineOrder.add(CARLINE_RS);
        carlineOrder.add(CARLINE_TT_RS);
        return ImmutableList.copyOf(carlineOrder);
    }
}
