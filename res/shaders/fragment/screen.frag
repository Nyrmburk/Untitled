varying vec4 vertColor;
varying vec3 normal;
varying vec3 vertexToLightColor;

void main(){
	
	// Defining The Material Colors
    const vec4 AmbientColor = vec4(0.1, 0.0, 0.0, 1.0);
    const vec4 DiffuseColor = vec4(1.0, 0.0, 0.0, 1.0);
 
    // Scaling The Input Vector To Length 1
    vec3 normalized_normal = normalize(normal);
    vec3 normalized_vertex_to_light_vector = normalize(vertexToLightColor);
 
    // Calculating The Diffuse Term And Clamping It To [0;1]
    float DiffuseTerm = clamp(dot(normal, vertexToLightColor), 0.0, 1.0);
 
    // Calculating The Final Color
    gl_FragColor = vertColor + vertColor * DiffuseTerm;
	//gl_FragColor = vertColor;
}