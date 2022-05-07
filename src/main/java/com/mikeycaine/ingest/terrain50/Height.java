package com.mikeycaine.ingest.terrain50;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Height {
    // OS Grid coords
    // height in metres
    public final int x;
    public final int y;
    public final float height;
}
