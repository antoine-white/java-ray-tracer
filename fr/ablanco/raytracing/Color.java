package fr.ablanco.raytracing;

public record Color (float r, float g, float b, float o) {
    public static  Color mult(Color c1, Color c2){
        return normalise(new Color(c1.r*c2.r, c2.g*c1.g,c2.b*c1.b , c1.o*c2.o));
    }
    public static Color add(Color c1, Color c2){
        return normalise(new Color(c1.r+c2.r, c2.g+c1.g,c2.b+c1.b, c1.o+c2.o));
    }
    public static  Color mult(Color c1, float scalar){
        return normalise(new Color(c1.r*scalar, scalar*c1.g,scalar*c1.b , c1.o));
    }

    private static Color normalise(Color c1){
        return new Color(Math.min(1f,c1.r), Math.min(1f,c1.g),Math.min(1f,c1.b) , Math.min(1f,c1.o));
    }

    @Override
    public String toString(){
        return "{r : " + r + ", g : " + g + ", b : " + b + ", opa : " + o  + "}";
    }

}