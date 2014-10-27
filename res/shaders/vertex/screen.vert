varying vec4 vertColor;
varying vec3 normal;
varying vec3 vertexToLightColor;

void main(){
    gl_Position = gl_ModelViewProjectionMatrix*gl_Vertex;
	vertColor = gl_Color;
	normal = gl_Normal;
	//vertexToLightColor = vec3(gl_LightSource[0].position (gl_ModelViewMatrix * gl_Vertex));
}