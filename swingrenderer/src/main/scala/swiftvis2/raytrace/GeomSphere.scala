package swiftvis2.raytrace

case class GeomSphere(center: Point, radius: Double, color: (Point) => RTColor, reflect: (Point) => Double) extends Geometry with Sphere {
  def movedBy(v: Vect): Sphere = copy(center = center+v)
  
  override def intersect(r: Ray): Option[IntersectData] = {
    intersectParam(r).flatMap { case (enter, exit) =>
      val inter = if (enter < 0) exit else enter
      if (inter < 0) None
      else {
        val pnt = r point inter
        val normal = (pnt - center).normalize
        Some(new IntersectData(inter, pnt, normal, color(pnt), reflect(pnt), this))
      }
    }
  }

  override def boundingSphere: Sphere = this

}
