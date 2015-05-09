/*
 * Copyright 2001-2015 Artima, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.scalatest.prop

import org.scalatest.FunSpec
import org.scalatest.Matchers

class RndSpec extends FunSpec with Matchers {

  describe("A Rnd") {
    it("should offer a nextInt method that produces the same values as java.util.Random given the same seed") {
      val jr = new java.util.Random(100)
      val ja = jr.nextInt()
      val jb = jr.nextInt()
      val jc = jr.nextInt()

      val sr = Rnd(100)
      val (ia, ra) = sr.nextInt
      val (ib, rb) = ra.nextInt
      val (ic, _) = rb.nextInt

      ja shouldEqual ia
      jb shouldEqual ib
      jc shouldEqual ic
    }

    it("should offer a nextLong method that produces the same values as java.util.Random given the same seed") {
      val jr = new java.util.Random(100)
      val ja = jr.nextLong()
      val jb = jr.nextLong()
      val jc = jr.nextLong()

      val sr = Rnd(100)
      val (ia, ra) = sr.nextLong
      val (ib, rb) = ra.nextLong
      val (ic, _) = rb.nextLong

      ja shouldEqual ia
      jb shouldEqual ib
      jc shouldEqual ic
    }

    it("should offer a nextFloatBetween0And1 method that produces the same values as java.util.Random given the same seed") {
      val jr = new java.util.Random(100)
      val ja = jr.nextFloat()
      val jb = jr.nextFloat()
      val jc = jr.nextFloat()

      val sr = Rnd(100)
      val (ia, ra) = sr.nextFloatBetween0And1
      val (ib, rb) = ra.nextFloatBetween0And1
      val (ic, _) = rb.nextFloatBetween0And1

      ja shouldEqual ia
      jb shouldEqual ib
      jc shouldEqual ic
    }

    it("should offer a nextDoubleBetween0And1 method that produces the same values as java.util.Random given the same seed") {
      val jr = new java.util.Random(100)
      val ja = jr.nextDouble()
      val jb = jr.nextDouble()
      val jc = jr.nextDouble()

      val sr = Rnd(100)
      val (ia, ra) = sr.nextDoubleBetween0And1
      val (ib, rb) = ra.nextDoubleBetween0And1
      val (ic, _) = rb.nextDoubleBetween0And1

      ja shouldEqual ia
      jb shouldEqual ib
      jc shouldEqual ic
    }

    it("should offer a nextByteWithEdges method that initially produces Byte edge values") {
      val r0 = Rnd(100)
      val (a1, r1) = r0.nextByteWithEdges
      val (a2, r2) = r1.nextByteWithEdges
      val (a3, r3) = r2.nextByteWithEdges
      val (a4, r4) = r3.nextByteWithEdges
      val (a5, _) = r4.nextByteWithEdges
      val edges = List(a1, a2, a3, a4, a5)
      edges should contain (0)
      edges should contain (1)
      edges should contain (-1)
      edges should contain (Byte.MaxValue)
      edges should contain (Byte.MinValue)
    }

    it("should offer a nextShortWithEdges method that initially produces Short edge values") {
      val r0 = Rnd(100)
      val (a1, r1) = r0.nextShortWithEdges
      val (a2, r2) = r1.nextShortWithEdges
      val (a3, r3) = r2.nextShortWithEdges
      val (a4, r4) = r3.nextShortWithEdges
      val (a5, _) = r4.nextShortWithEdges
      val edges = List(a1, a2, a3, a4, a5)
      edges should contain (0)
      edges should contain (1)
      edges should contain (-1)
      edges should contain (Short.MaxValue)
      edges should contain (Short.MinValue)
    }

    it("should offer a nextCharWithEdges method that initially produces Char edge values") {
      val r0 = Rnd(100)
      val (a1, r1) = r0.nextCharWithEdges
      val (a2, _) = r1.nextCharWithEdges
      val edges = List(a1, a2)
      edges should contain (Char.MaxValue)
      edges should contain (Char.MinValue)
    }

    it("should offer a nextIntWithEdges method that initially produces Int edge values") {
      val r0 = Rnd(100)
      val (a1, r1) = r0.nextIntWithEdges
      val (a2, r2) = r1.nextIntWithEdges
      val (a3, r3) = r2.nextIntWithEdges
      val (a4, r4) = r3.nextIntWithEdges
      val (a5, _) = r4.nextIntWithEdges
      val edges = List(a1, a2, a3, a4, a5)
      edges should contain (0)
      edges should contain (1)
      edges should contain (-1)
      edges should contain (Int.MaxValue)
      edges should contain (Int.MinValue)
    }

    it("should offer a nextLongWithEdges method that initially produces Long edge values") {
      val r0 = Rnd(100)
      val (a1, r1) = r0.nextLongWithEdges
      val (a2, r2) = r1.nextLongWithEdges
      val (a3, r3) = r2.nextLongWithEdges
      val (a4, r4) = r3.nextLongWithEdges
      val (a5, _) = r4.nextLongWithEdges
      val edges = List(a1, a2, a3, a4, a5)
      edges should contain (0)
      edges should contain (1)
      edges should contain (-1)
      edges should contain (Long.MaxValue)
      edges should contain (Long.MinValue)
    }

    it("should offer a nextDoubleWithEdges method that initially produces Double edge values") {
      val r0 = Rnd(100)
      val (a1, r1) = r0.nextDoubleWithEdges
      a1 should be (0.0)
    }

    it("should offer a nextPosIntWithEdges method that initially produces PosInt edge values") {
      import org.scalactic.anyvals.PosInt
      val r0 = Rnd(100)
      val (a1, r1) = r0.nextPosIntWithEdges
      val (a2, _) = r1.nextPosIntWithEdges
      val edges = List(a1, a2)
      edges should contain (PosInt(1))
      edges should contain (PosInt.MaxValue)
    }

    it("should offer a chooseInt method that initially produces Int values between from and to") {
      import GenDrivenPropertyChecks._
      var rnd = Rnd.default
      forAll { (i: Int, j: Int) =>
        val (k, nextRnd) = rnd.chooseInt(i, j)
        val min = i.min(j)
        val max = i.max(j)
        k should be <= max
        k should be >= min
        rnd = nextRnd
      }
    }

    it("should offer a chooseLong method that initially produces Long values between from and to") {
      import GenDrivenPropertyChecks._
      var rnd = Rnd.default
      forAll { (i: Long, j: Long) =>
        val (k, nextRnd) = rnd.chooseLong(i, j)
        val min = i.min(j)
        val max = i.max(j)
        k should be <= max
        k should be >= min
        rnd = nextRnd
      }
    }
  }
}

