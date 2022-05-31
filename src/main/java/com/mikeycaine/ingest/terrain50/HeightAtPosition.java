package com.mikeycaine.ingest.terrain50;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@ToString
public class HeightAtPosition implements Comparable<HeightAtPosition>{
    // OS Grid coords
    // height in metres
    public final int x;
    public final int y;
    public final float height;
    public final String gridSquare;


    @Override
    public int compareTo(HeightAtPosition that) {
        float diff = height - that.height;
        if (diff < 0.0) {
            return -1;
        } else if (diff > 0.0) {
            return 1;
        } else {
            return 0;
        }
    }
}
