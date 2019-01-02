package swiftvis2.raytrace

import org.scalatest.FlatSpec
import org.scalatest.Matchers

class KDTreeGeometryTest extends FlatSpec with Matchers {
  "A KD Geometry Tree" should "have proper intersections for a small test" in {
    val spheres = List(GeomSphere(Point(0, 0, 0), 1, p => RTColor.White, p => 0.0),
                       GeomSphere(Point(3, 0, 0), 1, p => RTColor.White, p => 0.0),
                       GeomSphere(Point(-3, 0, 0), 1, p => RTColor.White, p => 0.0))
    val tree = new KDTreeGeometry(spheres, 1)
    val hit0 = tree.intersect(Ray(Point(0, -10, 0), Vect(0, 1, 0)))
    hit0.nonEmpty should be (true)
    hit0.get.geom should be (spheres(0))
    hit0.get.time should be (9.0)
    hit0.get.norm should be (Vect(0, -1, 0))

    val hit1 = tree.intersect(Ray(Point(0, -10, 0), Vect(-0.3, 1, 0).normalize))
    hit1.nonEmpty should be (true)
    hit1.get.geom should be (spheres(2))
    hit1.get.time should be (Math.sqrt(100+9)-1)

    val miss = tree.intersect(Ray(Point(0, -10, 0), Vect(-0.15, 1, 0).normalize))
    miss.nonEmpty should be (false)
  }
  
  it should "have proper intersect data for a large random test outside" in {
    val spheres = Array.fill(2000)(GeomSphere(Point(math.random, math.random, math.random), 0.01, p => RTColor.White, p => 0.0))
    val tree = new KDTreeGeometry(spheres)
    val list = new ListScene(spheres:_*)
    
    // Hits first
    for(s <- spheres) {
      val ray = Ray(Point(100, 0, 0), s.center)
      tree.intersect(ray) should be (list.intersect(ray))
    }
    
    // Random
    for(_ <- 1 to 100) {
      val ray = Ray(Point(100, 0, 0), Point(math.random, math.random, math.random))
      tree.intersect(ray) should be (list.intersect(ray))
    }
  }

  it should "have proper intersect data for a large random test inside" in {
    val spheres = Array.fill(2000)(GeomSphere(Point(math.random, math.random, math.random), 0.01, p => RTColor.White, p => 0.0))
    val tree = new KDTreeGeometry(spheres)
    val list = new ListScene(spheres:_*)
    
    // Hits first
    println("hits")
    for(s <- spheres) {
      val ray = Ray(Point(100, 0, 0), s.center)
      tree.intersect(ray) should be (list.intersect(ray))
    }
    
    // Random
    println("random")
    for(_ <- 1 to 100) {
      val ray = Ray(Point(math.random, math.random, math.random), Point(math.random, math.random, math.random))
      tree.intersect(ray) should be (list.intersect(ray))
    }
  }
}