/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.scalatest.{Matchers, WordSpecLike}


/**
 * @author Marek Lewandowski <marek.m.lewandowski@gmail.com>
 * @since 11/07/15
 */
class PicklingBugTest extends WordSpecLike with Matchers
{
  "Pickling" must {

    import scala.pickling.Defaults._, scala.pickling.json._

    "pickle java.lang.Class to JSON" in {
      val clazz: Class[Int] = classOf[Int]

      val pickle = clazz.pickle

      pickle.value should not be 'isEmpty

//      {
//        "$type": "java.lang.Class[scala.Int]"
//      }
    }

    "unpickle java.lang.Class from JSON" in {
      val clazz: Class[Int] = classOf[Int]

      val pickle = clazz.pickle

      val deserializedJsonPickle: JSONPickle = JSONPickle(pickle.value)
      val deserializedClass: Class[Int] = deserializedJsonPickle.unpickle[Class[Int]] // fails on this line
      deserializedClass shouldEqual clazz

// [info] - must unpickle java.lang.Class from JSON *** FAILED ***
// [info]   java.lang.IllegalAccessException: java.lang.Class
// [info]   at sun.misc.Unsafe.allocateInstance(Native Method)
// [info]   at PicklingBugTest$$anonfun$1$$anonfun$apply$mcV$sp$2$JavaLangClass$u005BscalaInt$u005DUnpickler$macro$3$2$.unpickle(PicklingBugTest.scala:52)
// [info]   at scala.pickling.Unpickler$class.unpickleEntry(Pickler.scala:79)
// [info]   at PicklingBugTest$$anonfun$1$$anonfun$apply$mcV$sp$2$JavaLangClass$u005BscalaInt$u005DUnpickler$macro$3$2$.unpickleEntry(PicklingBugTest.scala:52)
// [info]   at scala.pickling.functions$.unpickle(functions.scala:11)
// [info]   at scala.pickling.UnpickleOps.unpickle(Ops.scala:23)
// [info]   at PicklingBugTest$$anonfun$1$$anonfun$apply$mcV$sp$2.apply$mcV$sp(PicklingBugTest.scala:52)
// [info]   at PicklingBugTest$$anonfun$1$$anonfun$apply$mcV$sp$2.apply(PicklingBugTest.scala:46)
// [info]   at PicklingBugTest$$anonfun$1$$anonfun$apply$mcV$sp$2.apply(PicklingBugTest.scala:46)
// [info]   at org.scalatest.Transformer$$anonfun$apply$1.apply$mcV$sp(Transformer.scala:22)
    }
  }
}
