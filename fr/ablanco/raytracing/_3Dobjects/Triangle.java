package fr.ablanco.raytracing._3Dobjects;

import fr.ablanco.raytracing.Color;
import fr.ablanco.raytracing.Utils.Vec3f;

public class Triangle extends Plane{
    private Vec3f A;
    private Vec3f B;
    private Vec3f C;

    public Triangle(Vec3f a, Vec3f b, Vec3f c, Color color) {
        super(calculateNormal(a,b,c),a,color);
        A = a;
        B = b;
        C = c;
    }

    public Triangle(Vec3f a, Vec3f b, Vec3f c) {
        this(a, b, c, Plane.DEFAULT_COLOR);
    }

    private static Vec3f calculateNormal(Vec3f a, Vec3f b, Vec3f c){
        Vec3f AB = new Vec3f();
        AB.setSub(a, b);
        Vec3f AC = new Vec3f();
        AC.setSub(a, c);
        return Vec3f.crossProduct(AB, AC);
    }


    @Override
    public double getIntersection(Vec3f P, Vec3f v) {
        double lambdaI = super.getIntersection(P, v);
        if (lambdaI <= 0)
            return lambdaI;

        Vec3f I = new Vec3f((float)(v.x*lambdaI),(float)(v.y*lambdaI),(float)(v.z*lambdaI));
        I.setAdd(P, I);

        Vec3f IA = new Vec3f();
        IA.setSub(I,A);
        Vec3f IB = new Vec3f();
        IB.setSub(I,B);
        Vec3f IC = new Vec3f();
        IC.setSub(I,C);       

        float alpha = n.dotProduct(Vec3f.crossProduct(IB,IC));
        float beta = n.dotProduct(Vec3f.crossProduct(IC,IA));
        float gamma = n.dotProduct(Vec3f.crossProduct(IA,IB));
        
        if (alpha > 0 && beta > 0 && gamma > 0) 
            return lambdaI;
        else
            return -1;
    }

    @Override
    public float getReflexionIndex() {
        return 1f;
    }

}
