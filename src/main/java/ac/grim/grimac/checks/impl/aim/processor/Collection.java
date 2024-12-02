package ac.grim.grimac.checks.impl.aim.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Collection {
    public List<Double> collectedList = new ArrayList<>();
    int listSize = 5;
    boolean block0s = true;

    public Collection(int collectionSize, boolean block0) {
        listSize = collectionSize;
        block0s = block0;
    }

    public void addItem(double num) {
        if (num == 0 && block0s) return;

        collectedList.add(num);

        if (collectedList.size() >= listSize) {
            collectedList.remove(1);
        }
    }

    public void clear() {
        collectedList = new ArrayList<>();
    }

    public double getHighestRepeats(boolean exempt0) {
        HashMap<Double,Integer> repeats = new HashMap<>();

        for (double num : collectedList) {
            repeats.put(num,repeats.getOrDefault(num,0) +1);
        }

        double hr = 0;
        double ha = 0;
        for (Map.Entry entry : repeats.entrySet()) {
            if ((Integer) entry.getValue() > ha) {
                ha = (Integer) entry.getValue();
                hr = (Double) entry.getKey();
            }
        }

        return hr;
    }

    public double getNumOfHighestRepeats(boolean exempt0) {
        HashMap<Double,Integer> repeats = new HashMap<>();

        for (double num : collectedList) {
            repeats.put(num,repeats.getOrDefault(num,0) +1);
        }

        double ha = 0;
        for (Map.Entry entry : repeats.entrySet()) {
            if ((Integer) entry.getValue() > ha) {
                ha = (Integer) entry.getValue();
            }
        }

        return ha;
    }

    public double getAverage() {
        double avg = 0;

        for (double num : collectedList) {
            avg += num;
        }

        return avg /= collectedList.size();
    }

    public double getPeak() {
        double highest = -999;

        for (double object : collectedList) {
            if (object > highest) {
                highest = object;
            }
        }

        return highest;
    }

    public double getTrough() {
        double lowest = 999;

        for (double object : collectedList) {
            if (object < lowest) {
                lowest = object;
            }
        }

        return lowest;
    }

    public List<Double> getCollectedList() {
        return collectedList;
    }

    public int getListSize() {
        return collectedList.size();
    }
}
