package org.rivierarobotics.util;

public enum MotorGroup {
    FR(0, 1, Side.RIGHT, Side.FRONT),
    FL(2, 3, Side.LEFT, Side.FRONT),
    BL(4, 5, Side.LEFT, Side.BACK),
    BR(6, 7, Side.RIGHT, Side.BACK);

    public final int steerCANId, driveCANId;
    public final Side LRSide, FBSide;

    MotorGroup(int steerCANId, int driveCANId, Side LRSide, Side FBSide) {
        this.steerCANId = steerCANId;
        this.driveCANId = driveCANId;
        this.LRSide = LRSide;
        this.FBSide = FBSide;
    }

    public enum Side {
        LEFT, RIGHT, FRONT, BACK
    }
}
