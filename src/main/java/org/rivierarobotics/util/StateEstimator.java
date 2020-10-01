/*
 * This file is part of Swerve2020, licensed under the GNU General Public License (GPLv3).
 *
 * Copyright (c) Riviera Robotics <https://github.com/Team5818>
 * Copyright (c) contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.rivierarobotics.util;

import org.ejml.dense.row.RandomMatrices_DDRM;
import org.ejml.ops.MatrixIO;
import org.ejml.simple.SimpleMatrix;

import java.io.IOException;
import java.util.Random;

public class StateEstimator {
    private final MotorGroup groupId;
    private SimpleMatrix A; //system matrix
    private SimpleMatrix B; //input matrix
    private final SimpleMatrix C; //output matrix
    private SimpleMatrix K_d;
    private final SimpleMatrix Q; //process noise covariance matrix
    private final SimpleMatrix R; //measurement noise covariance matrix
    private SimpleMatrix x;
    private SimpleMatrix x_hat; //state estimate vector
    private SimpleMatrix y; //output vector
    private SimpleMatrix P; //error covariance matrix
    private SimpleMatrix Kk;
    private final SimpleMatrix Rs;
    private final SimpleMatrix vff;
    private final SimpleMatrix oNoiseVec;
    private final SimpleMatrix pNoiseVec;
    private static final double kvff = 0.10455; //TODO: make this not a magic number
    private static final double opNoise = Math.PI / 360;
    private static final int numTimeSteps = 20 + 1;
    private double lastPos; //module pos
    private double lastVel; //wheel speed

    public StateEstimator(MotorGroup groupId) {
        this.groupId = groupId;
        //State feedback matrices from files
        try {
            A = SimpleMatrix.wrap(MatrixIO.loadCSV("Ad_mat.txt", true));
            B = SimpleMatrix.wrap(MatrixIO.loadCSV("Bd_mat.txt", true));
            K_d = SimpleMatrix.wrap(MatrixIO.loadCSV("K_mat.txt", true));
        } catch (IOException e) {
            e.printStackTrace();
        }
        C = new SimpleMatrix(2, 4);
        C.set(0, 0, 1);
        C.set(0, 2, 1);
        C.set(1, 0, 1);
        C.set(1, 2, -1);

        //Kalman matrices
        Q = SimpleMatrix.identity(4).scale(opNoise);
        R = SimpleMatrix.identity(2).scale(opNoise);

        //Initialize dynamic matrices
        x = new SimpleMatrix(4, 1);
        x_hat = new SimpleMatrix(4, 1);
        y = C.mult(x);
        P = SimpleMatrix.identity(4);
        Kk = new SimpleMatrix(4, 2);

        Rs = new SimpleMatrix(4, 1);
        vff = new SimpleMatrix(2, 1);
        oNoiseVec = new SimpleMatrix(4, 1);
        pNoiseVec = new SimpleMatrix(4, 1);
    }

    public void update() {
        for (int t = 0; t < numTimeSteps; t++) {
            //closed loop setpoints
            Rs.set(0, Math.PI / 2);
            Rs.set(1, x_hat.get(1));
            Rs.set(2, x_hat.get(2));
            Rs.set(3, 28 * Math.PI);

            //calculating voltages
            vff.set(0, Rs.get(3) * kvff);
            vff.set(1, -Rs.get(3) * kvff);
            SimpleMatrix u = K_d.mult(Rs.minus(x)).plus(vff);

            //cap input
            u.set(0, Math.min(12, Math.abs(u.get(0))) * Math.signum(u.get(0)));
            u.set(1, Math.min(12, Math.abs(u.get(1))) * Math.signum(u.get(1)));

            //update system
            Random rand = new Random();
            RandomMatrices_DDRM.fillGaussian(pNoiseVec.getDDRM(), 0.0, opNoise, rand);
            RandomMatrices_DDRM.fillGaussian(oNoiseVec.getDDRM(), 0.0, opNoise, rand);
            x = A.mult(x).plus(B.mult(u)).plus(pNoiseVec);
            y = C.mult(x.plus(oNoiseVec));

            //kalman filter prediction
            SimpleMatrix P_ = A.mult(P).mult(A.transpose()).plus(Q);
            SimpleMatrix x_hat_ = A.mult(x_hat).plus(B.mult(u));

            //kalman update
            Kk = P_.mult(C.transpose()).mult((C.mult(P_).mult(C.transpose()).plus(R)).invert());
            x_hat = x_hat_.plus(Kk.mult(y.minus(C.mult(x_hat_))));
            P = P_.minus(Kk.mult(C).mult(P_));
        }
        lastPos = x.get(0, 0);
        lastVel = x.get(3, 0);
    }

    public double getPosition() {
        return lastPos;
    }

    public double getVelocity() {
        return lastVel;
    }
}
