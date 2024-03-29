/*
 * Created on 03/02/2005
 *
 * JRandTest package
 *
 * Copyright (c) 2005, Zur Aougav, aougav@hotmail.com
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, 
 * are permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice, this list 
 * of conditions and the following disclaimer. 
 * 
 * Redistributions in binary form must reproduce the above copyright notice, this 
 * list of conditions and the following disclaimer in the documentation and/or 
 * other materials provided with the distribution. 
 * 
 * Neither the name of the JRandTest nor the names of its contributors may be 
 * used to endorse or promote products derived from this software without specific 
 * prior written permission. 
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR 
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON 
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.fasteasytrade.jrandtest.tests;

import java.util.Map;
import java.util.logging.Logger;

import com.fasteasytrade.jrandtest.utils.Format;

/**
 * The Monte Carlo test. Read x 16 bits, y 16 bits. if (x,y) distance is
 * larger then (256,256), we count a miss... the (x,y) point is not in the
 * circle(256). Else, we count success.
 * 
 * <p> piValue is (success / num_of_points) * 4.
 * 
 * @author Zur Aougav
 */

public class MonteCarlo extends Base {

    final private Logger log = Logger.getLogger(getClass().getName());

    /**
     */
    @Override
    public ResultStatus test(Map<String,String> details) {
        final int square256 = 256 * 256; // square(radius) of circle(256)
        long success = 0;
        long length = 0;

        rs.openInputStream();

        byte[] b = new byte[4];
        int x, y;
        int i;

        while (true) {
            for (i = 0; i < 4; i++) {
                b[i] = rs.readByte();
                if (!rs.isOpen()) {
                    break;
                }
            }

            if (!rs.isOpen()) {
                break;
            }

            length++;

            x = ((0xff & b[0]) << 8) | (0xff & b[1]);
            y = ((0xff & b[2]) << 8) | (0xff & b[3]);

            /*
             * Is point(x,y) in circle(256) ?
             */
            if (x * x + y * y <= square256) {
                success++;
            }
        }

        rs.closeInputStream();

        log.info("\n\t found " + length + " points.");
        log.info("\n\t found " + success + " points in circle(256).");

        double piValue = ((double)success / length);
        piValue *= 4.0;
        log.info("\n\t piValue: " + Format.d4(piValue));

        return ResultStatus.UNKNOWN;
    }

}
